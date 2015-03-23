import java.util.concurrent.TimeUnit

import actors.TimedActor
import akka.actor.ActorSystem
import modules._
import play.api.libs.concurrent.Akka
import play.api.{Application, GlobalSettings}
import scaldi.Injector
import scaldi.akka.AkkaInjectable
import scaldi.play.ScaldiSupport
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.FiniteDuration

object Global extends GlobalSettings with ScaldiSupport with AkkaInjectable{

  override def applicationModule: Injector =
                                        new AkkaModule ::
                                        new ControllerModule ::
                                        new ServiceModule ::
                                        new NetworkModule ::
                                        new StorageModule ::
                                        new ProviderModule

  override def onStart(app: Application): Unit = {
    super.onStart(app)

    implicit val system = inject[ActorSystem]
//    Akka.system.scheduler.schedule(FiniteDuration.apply(10,TimeUnit.SECONDS), FiniteDuration.apply(10,TimeUnit.SECONDS),  injectActorRef[TimedActor], "ping")
  }

}