package fr.upem.geoplan.core.server.gcm;

/**
 * Created by Jeremie on 08/03/2016.
 */
public class DataConstantGcm {
    //General constant
    public static String KEY_MSG_ID = "messageId";
    public static String ACTION = "action";
    public static String POSITION_LATITUDE = "lat";
    public static String POSITION_LONGITUDE = "lng";

    // User constant
    public static String USER_ID = "_id";
    public static String FIRST_NAME = "firstName";
    public static String LAST_NAME = "lastName";
    public static String PHONE = "phone";
    public static String EMAIL = "email";

    // Event constant
    public static String EVENT_ID =  "_id";
    public static String EVENT_NAME = "name";
    public static String EVENT_DESCRIPTION = "description";
    public static String EVENT_LOCALIZATION = "localization";
    public static String EVENT_START_DATE_TIME =  "startDateTime";
    public static String EVENT_END_DATE_TIME =  "endDateTime";
    public static String EVENT_OWNERS_ID =  "owners";
    public static String EVENT_GUESTS_ID =  "guests";
    public static String EVENT_WEIGHT =  "weight";
    public static String EVENT_TYPE =  "type";
    public static String EVENT_COST =  "cost";
    public static String EVENT_COLOR =  "color";

    // Action method
    public static String ACTION_CREATE_USER = "createUser";
    public static String ACTION_CREATE_EVENT = "createEvent";
    public static String ACTION_GET_ALL_EVENTS_OWNED = "getAllEventsOwned";
    public static String ACTION_GET_ALL_EVENTS_GUESTED = "getAllEventsGuested";
    public static String ACTION_GET_USERS = "getUsers()";
    public static String ACTION_UPDATE_POSITION = "updatePosition";
    public static String ACTION_ADD_USERS_TO_EVENT = "addUserToEvent";
    public static String ACTION_REMOVE_USERS_TO_EVENT = "removeUserToEvent";
    public static String ACTION_UPDATE_EVENT = "updateEvent";
    public static String ACTION_UPDATE_USER = "updateUser";
    public static String ACTION_GET_POSITION_USER ="askUserPositions";
}
