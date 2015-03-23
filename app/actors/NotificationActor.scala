package actors

import akka.actor.Actor
import model._
import play.api.Logger
import scaldi.Injector
import scaldi.akka.AkkaInjectable
import services.GcmService

class NotificationActor(implicit inj: Injector) extends Actor with AkkaInjectable{

  // faelle ->
  // config failed
  // send events by push/mail

  val gcmService = inject[GcmService]

  override def receive: Receive = {
    case data: (List[UiTimetableEvent],UiUserBundle) => notifyNewEvents(data._1, data._2)
    case data: (UiUserBundle) =>notifyInvalidConfig(data)
  }

  def notifyNewEvents(events: List[UiTimetableEvent], userBundle: UiUserBundle): Unit ={
    Logger.info("new news")
    events.foreach(e => Logger.info("notifcation: " + e.toString))

    gcmService.sendGcmUpdatedTimetable(userBundle, events)
  }

  def notifyInvalidConfig(config: UiUserBundle): Unit = {
    Logger.info("invalid config")

    gcmService.sendGcmInvalidConfig(config)

  }

}
