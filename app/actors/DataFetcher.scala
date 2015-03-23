package actors

import akka.actor.Actor
import model.{MergedTimetablePeriod, UiUserBundle}
import play.api.Logger
import provider.{WebUntisProvider, UserProvider}
import scaldi.Injector
import scaldi.akka.AkkaInjectable
import services.WebUntisService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.{TimetableUtil, JsonUtil}

import scala.concurrent.Future

class DataFetcher(implicit inj: Injector) extends Actor with AkkaInjectable{

  val userProvider = inject[UserProvider]
  val webUntisProvider = inject[WebUntisProvider]
  val analystActor = injectActorRef[AnalystActor]
  val notificationActor = injectActorRef[NotificationActor]

  override def receive: Receive = {
    case user: UiUserBundle => doSomeStuff(user)
  }

  def doSomeStuff(uiBundle: UiUserBundle) = {
    val config = uiBundle.uiTimetableConfig
    Logger.info(s"load data: ${config}")

    webUntisProvider.loadTimetable(config.url, config.school, config.userName, config.password, config.elementType, config.elementId).map{ data =>
      data match {
        case Some(timetable) => {
          Logger.info(timetable.toString())

          val flatList = timetable.flatten

          if(!flatList.isEmpty){
            val irregularLesson = flatList.map( d => TimetableUtil.getIrregularEvents(d.data)).flatten
            val elements = flatList.map(d => d.data.elements).flatten.distinct

            analystActor ! (irregularLesson.map(d => MergedTimetablePeriod(d, elements)), uiBundle)

          }else{
            Logger.error(s"Error auth (1): ${uiBundle.uiUser.email}")
            userProvider.setUserBundleFailed(uiBundle)
          }
        }
        case None => {
          Logger.error(s"Error auth (2): ${uiBundle.uiUser.email}")
          userProvider.setUserBundleFailed(uiBundle)

          //notify user
          notificationActor ! (uiBundle)
        }
      }

    }
  }

}
