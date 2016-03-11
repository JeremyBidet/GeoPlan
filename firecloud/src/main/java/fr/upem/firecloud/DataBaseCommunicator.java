package fr.upem.firecloud;

import com.google.android.gms.maps.model.LatLng;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataBaseCommunicator {

    private final MongoCollection<Document> users;
    private final MongoCollection<Document> events;

    protected DataBaseCommunicator(){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("GeoPlan");
        users = database.getCollection("users");
        events = database.getCollection("events");
//        TODO Delete this when the app is clean
        users.drop();
        events.drop();
    }

    public void createUser(Map<String, Object> userJSON) {
        try {
            users.insertOne(new Document(userJSON));
        }catch (MongoWriteException e){
//            Do nothing ? User already in the base...
        }
    }

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

    public void updatePosition(Map<String, Object> userPosition){
        String id = (String) userPosition.remove("userId");
        users.updateOne(new Document("_id", id), new Document("$set", userPosition));
    }

    public List<String> getOwnerDevices(String eventId){
        Document event = events.find(new Document("_id", eventId)).first();
        ArrayList<Document> ownersId = (ArrayList<Document>)event.get("owners");
        List<String> devices = new LinkedList<>();
        for(Document objectId : ownersId){
            String id = (String) objectId.get("_id");
            Document userFromId = users.find(new Document("_id", id)).first();
            devices.add((String) userFromId.get("device"));
        }
        return devices;
    }

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


    public void addUserToEvent(Map<String, Object> userToEvent) {
        String eventId = (String)userToEvent.remove("eventId");
        events.updateOne(new Document("_id", eventId), new Document("$push", new Document("guest", new Document(userToEvent))));
    }


    public void removeUserToEvent(Map<String, Object> userToEvent) {
        String eventId = (String)userToEvent.remove("eventId");
        events.updateOne(new Document("_id", eventId), new Document("$pull", new Document("guest", new Document(userToEvent))));
    }

    public void updateEvent(Map<String, Object> eventJSON) {
        events.updateOne(new Document("_id", eventJSON.get("_id")), new Document("$push", eventJSON));
    }

    public void updateUser(Map<String, Object> userJSON) {
        users.updateOne(new Document("_id", userJSON.get("_id")), new Document("$push", userJSON));
    }

    public Map<String, Object> getAllEventsOwned(final String userId){
        final Map<String, Object> eventsOwned = new HashMap<>();
        final List<Map<String, Object>> eventsToAdd = new ArrayList<>();
        this.events.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
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

    public Map<String, Object> getAllEventsGuested(final String userId){
        final Map<String, Object> eventsGuested = new HashMap<>();
        final List<Map<String, Object>> eventsToAdd = new ArrayList<>();
        this.events.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
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
