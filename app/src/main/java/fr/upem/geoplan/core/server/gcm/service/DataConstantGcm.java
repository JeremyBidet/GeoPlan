package fr.upem.geoplan.core.server.gcm.service;

final class DataConstantGcm {

    private DataConstantGcm() {

    }

    //General constant
    static String KEY_MSG_ID = "messageId";
    static String ACTION = "action";
    static String POSITION_LATITUDE = "lat";
    static String POSITION_LONGITUDE = "lng";

    // User constant
    static String USER_ID = "_id";
    static String FIRST_NAME = "firstName";
    static String LAST_NAME = "lastName";
    static String PHONE = "phone";
    static String EMAIL = "email";

    // Event constant
    static String EVENT_ID = "_id";
    static String EVENT_NAME = "name";
    static String EVENT_DESCRIPTION = "description";
    static String EVENT_LOCALIZATION = "localization";
    static String EVENT_START_DATE_TIME = "startDateTime";
    static String EVENT_END_DATE_TIME = "endDateTime";
    static String EVENT_OWNERS_ID = "owners";
    static String EVENT_GUESTS_ID = "guests";
    static String EVENT_WEIGHT = "weight";
    static String EVENT_TYPE = "type";
    static String EVENT_COST = "cost";
    static String EVENT_COLOR = "color";

    // Action method sended to server
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

    public static String RECEIVED_EVENT_ID = "receivedEventID";
    public static String UPDATE_POSITION= "updatePosition";
    public static String RECEIVED_USER_POSITION = "receivedUserPosition";
    public static String RECEIVED_EVENTS_GUESTED = "receivedEventsGuested";
    public static String RECEIVED_EVENTS_OWNED = "receivedEventsOwned";
    public static String RECEIVED_USER_ACCORDING_TO_EMAIL = "receivedUserAccordingToEmail";

}
