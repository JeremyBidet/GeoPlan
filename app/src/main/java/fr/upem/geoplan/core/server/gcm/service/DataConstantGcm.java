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
    static String ACTION_CREATE_USER = "createUser";
    static String ACTION_CREATE_EVENT = "createEvent";
    static String ACTION_GET_ALL_EVENTS_OWNED = "getAllEventsOwned";
    static String ACTION_GET_ALL_EVENTS_GUESTED = "getAllEventsGuested";
    static String ACTION_GET_USER_ACCORDING_TO_EMAIL = "getUserAccordingToEmail";
    static String ACTION_UPDATE_POSITION = "updatePosition";
    static String ACTION_ADD_USERS_TO_EVENT = "addUserToEvent";
    static String ACTION_REMOVE_USERS_TO_EVENT = "removeUserToEvent";
    static String ACTION_UPDATE_EVENT = "updateEvent";
    static String ACTION_UPDATE_USER = "updateUser";
    static String ACTION_GET_POSITION_USER ="askUserPositions";

    static String RECEIVED_EVENT_ID = "receivedEventID";
    static String UPDATE_POSITION= "updatePosition";
    static String RECEIVED_USER_POSITION = "receivedUserPosition";
    static String RECEIVED_EVENTS_GUESTED = "receivedEventsGuested";
    static String RECEIVED_EVENTS_OWNED = "receivedEventsOwned";
    static String RECEIVED_USER_ACCORDING_TO_MAIL = "receivedUserAccordingToMail";

}
