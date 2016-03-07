package fr.upem.firecloud;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
/**
 * Created by Jeremie on 07/03/2016.
 */
public class MainTest {
    private static String DATABASE_URL= "https://blazing-inferno-2418.firebaseio.com/";
    public static void launch() {
        ServerApp server = new ServerApp(DATABASE_URL);

        ArrayList<Long> guests = new ArrayList<>();
        ArrayList<Long> owners = new ArrayList<>();
        long startDateEvent = new Date(2016-1900, 3, 6, 17,0).getTime();
        long endDateEvent = new Date(2016-1900, 3, 6, 20,0).getTime();

        String userjchattou = "{\n" +
                "\t\"1\": {\n" +
                "\t\t\"firstName\": \"Jeremie\",\n" +
                "\t\t\"lastName\": \"Chattou\",\n" +
                "\t\t\"position\": {\n" +
                "\t\t\t\"lat\": 2.3,\n" +
                "\t\t\t\"lon\": 3.4\n" +
                "\t\t},\n" +
                "\t\t\"phone\": \"01020202020\"\n" +
                "\t}\n" +
                "}";
        String usertfautrel = "{\n" +
                "\t\"2\": {\n" +
                "\t\t\"firstName\": \"Tristan\",\n" +
                "\t\t\"lastName\": \"Fautrel\",\n" +
                "\t\t\"position\": {\n" +
                "\t\t\t\"lat\": 2.3,\n" +
                "\t\t\t\"lon\": 3.4\n" +
                "\t\t},\n" +
                "\t\t\"phone\": \"00220202020\"\n" +
                "\t}\n" +
                "}";
        String eventAndroid = "{\n" +
                "\t\"1\": {\n" +
                "\t\t\"ownerId\": 1,\n" +
                "\t\t\"title\": \"Rendez-vous Android\",\n" +
                "\t\t\"description\": \"fighting!\",\n" +
                "\t\t\"guestsId\": [{\n" +
                "\t\t\t\"guestId\": 1\n" +
                "\t\t}, {\n" +
                "\t\t\t\"guestId\": 2\n" +
                "\t\t}],\n" +
                "\t\t\"startDateEvent\": \"06-03-2016 16:30\",\n" +
                "\t\t\"endDateEvent\": \"06-03-2016 19:30\",\n" +
                "\t\t\"localization\": \"Chez Jérémie\",\n" +
                "\t\t\"position\": {\n" +
                "\t\t\t\"latitude\": 48.838571,\n" +
                "\t\t\t\"longitude\": 2.561343\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        /*try {
            @SuppressWarnings("unchecked")
            Map <String, Object> mapJsonjchattou = (Map<String, Object>) JSONValue.parseWithException(userjchattou);
            Map <String, Object> mapJsontfautrel = (Map<String, Object>) JSONValue.parseWithException(usertfautrel);
            Map <String, Object> mapeventAndroid = (Map<String, Object>) JSONValue.parseWithException(eventAndroid);
            server.createUser(mapJsonjchattou);
            server.createUser(mapJsontfautrel);
            server.createEvent(mapeventAndroid);
        } catch (ParseException e) {
            Log.e("MainActivity", "pfff");
            e.printStackTrace();
        }*/


        server.createUser(1, "GSM_Samsung", "Jérémie", "Chattou", new LatLng(48.8385709, 2.561343), "0658596324");
        server.createUser(2, "GSM_Sony", "Tristan", "Fautrel", new LatLng(48.877535, 2.59016), "0621185284");
        server.createUser(3, "GSM_Nexus", "Huy", "Huynh", new LatLng(48.877535, 2.50016), "0621136584");

        //update position user (Huy)
        server.updatePositionUser(new LatLng(48.877535, 2.50016), 3);

        // création event
        owners.add(guests.get(0));
        server.createEvent(1, owners, "Rendez-vous Android", "ça va carburer ou pas!", guests, startDateEvent, endDateEvent, "Chez Jérémie", new LatLng(48.838571, 2.561343));

        /*//ajout utilisateur dans event
        server.addUserToEvent(userIdTristan, 1);

        //modification paramètre event
        guests.add(userIdJeremie);
        guests.add(userIdTristan);
        guests.add(userIdHuy);
        server.updateEvent(1, null, "Projet X", "Fêter la réussite du diplôme", guests, 0, 0, "Villa de Jérémie", null);

        //suppression utilisateur event
        server.removeUserToEvent(userIdHuy, 1);*/

    }
}
