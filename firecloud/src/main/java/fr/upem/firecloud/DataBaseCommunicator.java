package fr.upem.firecloud;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is used to communicate with the local Mongo database.
 */
public class DataBaseCommunicator {

    private final MongoCollection<Document> users;
    private final MongoCollection<Document> events;

    /**
     * Creates a new instance of the DataBaseCommunicator.
     * This class will create a new Data Base named "GeoPlan" if none exists.
     * It will also create two collections : one named "users" and the other "events"
     */
    public DataBaseCommunicator(){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("GeoPlan");
        users = database.getCollection("users");
        events = database.getCollection("events");
//        TODO Delete this when the app is clean
        users.drop();
        events.drop();
    }

    /**
     * Inserts the Json in the database to create a new User.
     * The _id field is required.
     * @param userJSON the Json to add in the database.
     */
    public void createUser(Map<String, Object> userJSON) {
        try {
            users.insertOne(new Document(userJSON));
        }catch (MongoWriteException e){
//            Do nothing ? User already in the base...
        }
    }

    /**
     * Inserts the Json in the database to create a new Event.
     * The _id is created automatically.
     * @param eventJSON the Json to add in the database.
     * @return a new Json map containing the _id field of the event.
     */
    public Map<String, Object> createEvent(Map<String, Object> eventJSON) {
        try {
            Document document = new Document(eventJSON);
            events.insertOne(document);
            HashMap<String, Object> map = new HashMap<>();
            map.put("_id", document.get("_id"));
            return map;
        }catch (MongoWriteException e){
            return null;
//            Do nothing ? Event already in the base...
        }
    }

    /**
     * Updates or inserts if there is none the position of the user in the database.
     * @param userPosition The json corresponding to the position.
     */
    public void updatePosition(Map<String, Object> userPosition){
        String id = (String) userPosition.remove("userId");
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.upsert(true);
        users.updateOne(new Document("_id", id), new Document("$set", userPosition), updateOptions);
    }

    /**
     * Get the device of the owner(s) of the event.
     * The device is a field to identify in gcm to who send the message.
     * @param eventId The eventId of which the device of the owner(s) is wanted.
     * @return The list of the owner(s) devices.
     */
    public List<String> getOwnerDevices(String eventId){
        Document event = events.find(new Document("_id", eventId)).first();
        @SuppressWarnings("unchecked")
        ArrayList<Document> ownersId = (ArrayList<Document>)event.get("owners");
        List<String> devices = new LinkedList<>();
        for(Document objectId : ownersId){
            String id = (String) objectId.get("_id");
            Document userFromId = users.find(new Document("_id", id)).first();
            devices.add((String) userFromId.get("device"));
        }
        return devices;
    }


    /**
     * Get the Json map of the user in the database.
     * @param userId the _id of the user.
     * @return A Json map representing the user.
     */
    public Map<String, Object> getUser(String userId){
        Document user = users.find(new Document("_id", userId)).first();
        HashMap<String, Object> userPayload = new HashMap<>();
        userPayload.put("_id", user.get("_id"));
        userPayload.put("email", user.get("email"));
        userPayload.put("firstName", user.get("firstName"));
        userPayload.put("lastName", user.get("lastName"));
        userPayload.put("phone", user.get("phone"));
        return userPayload;
    }


    /**
     * Adds a new _id of User in the guest array of an Event.
     * @param userToEvent a Json map with the userId and the eventId
     */
    public void addUserToEvent(Map<String, Object> userToEvent) {
        String eventId = (String)userToEvent.remove("eventId");
        events.updateOne(new Document("_id", eventId), new Document("$push", new Document("guest", new Document(userToEvent))));
    }


    /**
     * Removes an _id of User in the guest array of an Event.
     * @param userToEvent a Json map with the userId and the eventId
     */
    public void removeUserToEvent(Map<String, Object> userToEvent) {
        String eventId = (String)userToEvent.remove("eventId");
        events.updateOne(new Document("_id", eventId), new Document("$pull", new Document("guest", new Document(userToEvent))));
    }

    /**
     * Updates the Event fields.
     * @param eventJSON a Json map representing the Event
     */
    public void updateEvent(Map<String, Object> eventJSON) {
        events.updateOne(new Document("_id", eventJSON.get("_id")), new Document("$push", eventJSON));
    }

    /**
     * Updates the User fields.
     * @param userJSON a Json map representing the User.
     */
    public void updateUser(Map<String, Object> userJSON) {
        users.updateOne(new Document("_id", userJSON.get("_id")), new Document("$push", userJSON));
    }

    /**
     * Get a Json list of all events owned by the user.
     * @param userId the id of the user.
     * @return a Json map containing the list of the events owned by the user
     */
    public Map<String, Object> getAllEventsOwned(final String userId){
        final Map<String, Object> eventsOwned = new HashMap<>();
        final List<Map<String, Object>> eventsToAdd = new ArrayList<>();
        this.events.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                @SuppressWarnings("unchecked")
                ArrayList<Document> owners = (ArrayList<Document>) document.get("owners");
                for (Document owner : owners) {
                    if (owner.get("_id").equals(userId)) {
                        eventsToAdd.add(document);
                        break;
                    }
                }
            }
        });
        eventsOwned.put("events", eventsToAdd);
        return eventsOwned;
    }

    /**
     * Get a Json list of all Events where the user is a guest.
     * @param userId the id of the user.
     * @return a Json map containing the list of the events where the user is a guest
     */
    public Map<String, Object> getAllEventsGuested(final String userId){
        final Map<String, Object> eventsGuested = new HashMap<>();
        final List<Map<String, Object>> eventsToAdd = new ArrayList<>();
        this.events.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                @SuppressWarnings("unchecked")
                ArrayList<Document> owners = (ArrayList<Document>) document.get("guested");
                for (Document owner : owners) {
                    if (owner.get("_id").equals(userId)) {
                        eventsToAdd.add(document);
                        break;
                    }
                }
            }
        });
        eventsGuested.put("events", eventsToAdd);
        return eventsGuested;
    }

    /**
     * Get a Json list of all the users in the database.
     * @return a Json map containing the list of the the users in the database.
     */
    public Map<String, Object> getUsers(){
        final Map<String, Object> usersMap = new HashMap<>();
        final List<Map<String, Object>> usersList = new ArrayList<>();
        this.users.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Document copy = new Document(document);
                copy.remove("device");
                copy.remove("lat");
                copy.remove("lng");
                usersList.add(copy);
            }
        });
        usersMap.put("users", usersList);
        return usersMap;
    }

}
