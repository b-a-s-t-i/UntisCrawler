package services

import java.util.UUID

import model.{UiTimetableEvent, UiUserBundle, UserNotification}
import scaldi.{Injectable, Injector}


trait GcmService{
  val TYPE_UPDATE = "new_events"
  val TYPE_INVALID_CONFIG = "invalid_config"

  def sendGcmUpdatedTimetable(userBundle: UiUserBundle, events: List[UiTimetableEvent])
  def sendGcmInvalidConfig(userBundle: UiUserBundle)
}

class GcmServiceImpl(implicit inj: Injector) extends GcmService with Injectable{
  val network = inject[Network]

  override def sendGcmUpdatedTimetable(userBundle: UiUserBundle, events: List[UiTimetableEvent]): Unit = {
    sendPushNotification(getRegIds(userBundle), TYPE_UPDATE, events.map(_.eventId))
  }

  override def sendGcmInvalidConfig(userBundle: UiUserBundle): Unit = {
    sendPushNotification(getRegIds(userBundle),TYPE_INVALID_CONFIG, List(userBundle.uiTimetableConfig.configId))
  }

  private def getRegIds(uiUserBundle: UiUserBundle): List[String] = {
    uiUserBundle.uiUserNotification.filter(_.typee == UserNotification.TYPE_PUSH).map(_.address)
  }

  private def sendPushNotification(regIds: List[String], notificationType: String, ids: List[UUID]): Unit ={
    network.sendGcm(regIds, notificationType, ids.map(_.toString))
  }

}
