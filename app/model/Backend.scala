package model

import java.util.UUID

import org.joda.time.DateTime

case class User(email: String,
                userId: UUID,
                timeStampCreated: DateTime
               )

object User{
  final val TABLE = "user"
  final val EMAIL_KEY = "email"
  final val ID_KEY = "id"
  final val TIMESTAMP_CREATED_KEY = "dateTime"

  final val EMAIL_IDX = "email_index"
}

case class TimetableConfig(
                        configId: UUID,
                        userId: UUID,
                        url: String,
                        school: String,
                        elementType: Int,
                        elementId: Int,
                        userName: String,
                        password: String,
                        error: Boolean
                        )

object TimetableConfig{
  final val ID_KEY = "id"
  final val USER_ID_KEY = "uid"
  final val TABLE = "untisconfig"
  final val URL_KEY = "url"
  final val SCHOOL_KEY = "school"
  final val ELEMENT_TYPE_KEY = "elementType"
  final val ELEMENT_ID_KEY = "elementId"
  final val USER_KEY = "untisuser"
  final val PASSWORD_KEY = "untispassword"
  final val ERROR_KEY = "error"

  final val USER_ID_FK = "userIdFk"
}

case class TimetableEvent(
                         eventId: UUID,
                         userId: UUID,
                         configId: UUID,
                         createdAt: DateTime,
                         rawJsonData: String
                         )

object TimetableEvent{
  final val ID_KEY = "id"
  final val USER_ID_KEY = "userId"
  final val CONFIG_ID_KEY = "configId"
  final val DATETIME_KEY = "datetime"
  final val RAW_TIMETABLE_EVENT = "timetableEvent"
  final val TABLE = "timetableevents"

  final val FK_USER = "fk_user"
  final val FK_CONFIG = "fk_config"
}


case class UserNotification(
                           notificationId: UUID,
                           userId: UUID,
                           typee: Int,
                           address: String
                             )

object UserNotification{
  final val TABLE = "usernotification"
  final val ID_KEY = "id"
  final val USER_ID = "userId"
  final val TYPE_KEY = "type"
  final val ADDRESS_KEY = "address"

  final val FK_USER = "fk_user"

  final val TYPE_PUSH = 1;
}