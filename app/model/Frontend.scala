package model

import java.util.UUID

import org.joda.time.DateTime
import utils.JsonUtil

case class UiUser(email: String, userId: UUID, timeStampCreated: DateTime)
object UiUser{
  def apply(u: User): UiUser = UiUser(u.email, u.userId, u.timeStampCreated)
}

case class UiTimetableConfig(configId: UUID,userId: UUID,url: String,school: String,elementType: Int,elementId: Int, userName: String, password: String, error: Boolean)
object UiTimetableConfig{
  def apply(c: TimetableConfig): UiTimetableConfig = UiTimetableConfig(c.configId, c.userId, c.url, c.school, c.elementType, c.elementId, c.userName, c.password, c.error)
}
case class UiUserBundle(uiUser: UiUser, uiTimetableConfig: UiTimetableConfig)

case class UiTimetableEvent(eventId: UUID, userId: UUID, configId: UUID, createdAt: DateTime, timeTableData: MergedTimetablePeriod)
object UiTimetableEvent{
  def apply(e: TimetableEvent): UiTimetableEvent = UiTimetableEvent(e.eventId, e.userId, e.configId, e.createdAt, JsonUtil.jsonToMergedTimetablePeriod(e.rawJsonData).get)
}