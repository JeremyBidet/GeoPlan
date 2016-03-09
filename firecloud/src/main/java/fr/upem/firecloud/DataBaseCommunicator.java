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

    public Map<String, String> createEvent(Map<String, Object> eventJSON) {
        try {
            Document document = new Document(eventJSON);
            events.insertOne(document);
            HashMap<String, String> map = new HashMap<>();
            map.put("_id", (String) document.get("_id"));
            return map;
        }catch (MongoWriteException e){
            return null;
//            Do nothing ? Event already in the base...
        }
    }

    public void pushPosition(Map<String, Object> userPosition){
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


    public ArrayList<Long> getAllEvents(long userId) {
        return null;
    }

    public void addUserToEvent(Map<String, Object> userToEvent) {
        String eventId = (String)userToEvent.remove("eventId");
        events.updateOne(new Document("_id", eventId), new Document("$push", new Document("guest", new Document(userToEvent))));
    }

    public void addUsersToEvent(List<Long> guestsId, long eventId) {
    }

    public void removeUserToEvent(Map<String, Object> userToEvent) {
        String eventId = (String)userToEvent.remove("eventId");
        events.updateOne(new Document("_id", eventId), new Document("$pull", new Document("guest", new Document(userToEvent))));
    }

    public void removeUsersToEvent(List<Long> guestsId, long eventId) {

    }

    public void updateEvent(long eventId, Map<String, Object> eventJSON) {
    }

    public void updateEvent(long eventId, ArrayList<Long> ownersId, String title, String description, ArrayList<Long> guestsId, long startDateTime, long endDateTime, String localization, LatLng position) {
    }
}
