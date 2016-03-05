CreateEvent request from android :

'''json
{
  "category" : "fr.upem.geoplan.server.gcm.service",
  "data" : {
    "action" : "createEvent",
    "event" : {
      "owner" : "something to identify the user in the DB",
      "title" : "title",
      "guests" : [
        {
          "userId" : "something to identify the user in the DB"
        },
        {
          "userId" : "something to identify the user in the DB"
        }
      ],
      "start_date_time" : "date",
      "end_date_time" : "date2",
      "localization" : "localization",
      "position": {
              "lat": 2.3, //double
              "lon": 3.4 //double
            },
    },
    "refresh" : 1234
  },
  "message-id" : "id",
  "from" : "from"
}
'''

Request from server :

Only an ACK

createUser request from android :
'''json
{
"category" : "fr.upem.geoplan.server.gcm.service",
"data" : {
  "action" : "createUser",
  "userId" : 192,
  "firstName" : "kjzdkj",
  "lastName" : "kjzqkjz",
  "position": {
        "lat": 2.3, //double
        "lon": 3.4 //double
      },
      "phoneNumber" : 01020202020
}
}
'''

Request from server :

Only an ACK

getPosition request from android :
'''json
{
  "category" : "fr.upem.geoplan.server.gcm.service",
  "data" : {
    "action" : "getPosition",
    "userId" : userId
  }
}
'''

Answer from server :
'''json
{
  "data" : {
    "action" : "answerGetPosition",
    "position":
    {
       "lat": 2.3, //double
       "lon": 3.4 //double
    }
  },
  "to" : "deviceId"
}
'''


addUserToEvent request from android :
'''json
{
  "category" : "fr.upem.geoplan.server.gcm.service",
  "data" : {
    "action" : "addUserToEvent",
    "userId" : userId,
    "eventId" : eventId
  }
}
'''

Request from server :

ACK only

removeUserToEvent request from android :
'''json
{
  "category" : "fr.upem.geoplan.server.gcm.service",
  "data" : {
    "action" : "removeUserToEvent",
    "userId" : userId,
    "eventId" : eventId
  }
}
'''

changeStartDateTimeEvent request from android :

'''json
{
  "category" : "fr.upem.geoplan.server.gcm.service",
  "data" : {
    "action" : "changeStartDateTimeEvent",
    "eventId" : eventId,
    "startDateTime" : "date"
  }
}
'''

changeEndDateTimeEvent request from android :

'''json
{
  "category" : "fr.upem.geoplan.server.gcm.service",
  "data" : {
    "action" : "changeStartDateTimeEvent",
    "eventId" : eventId,
    "endDateTime" : "date"
  }
}
'''

getAllEvents request from android :
'''json
{
  "category" : "fr.upem.geoplan.server.gcm.service",
  "data" : {
    "action" : "getAllEvents",
    "userId" : userId
  }
}
'''