package provider

import java.util.UUID

import model._
import org.joda.time.DateTime
import play.api.Logger
import scaldi.{Injector, Injectable}
import services.{UserNotificationService, TimetableConfigService, UserService}


trait UserProvider{
  def getActivatedUser(): List[UiUserBundle]
  def addUser(email: String): Boolean
  def isEmailRegistered(email: String): Boolean
  def getUserByEmail(email: String): Option[UiUser]
  def setUserBundleFailed(uiUserBundle: UiUserBundle): Unit
  def addTimetableConfig(userId: UUID, server: String, school: String, user: String, password: String, elmentId: Int, elmentType: Int): Unit
  def addUserPushNotification(userId: UUID, pushId: String): Unit
}

class UserProviderImpl(implicit inj: Injector) extends UserProvider with Injectable{

  val userService: UserService = inject[UserService]
  val timetableConfigService: TimetableConfigService = inject[TimetableConfigService]
  val userNotificationService: UserNotificationService = inject[UserNotificationService]

  override def getActivatedUser(): List[UiUserBundle] = {
    userService.getAllUser().map{ user =>
      (user, timetableConfigService.getTimetableConfigByUser(user.userId), userNotificationService.getNotificationsByUserId(user.userId))
    }.filter{ e =>
      (e._2.isDefined && !e._2.get.error)
    }.map{ e =>
      UiUserBundle(
        UiUser(e._1),
        UiTimetableConfig(e._2.get),
        e._3.map(UiUserNotification(_))
      )
    }
  }

  override def addUser(email: String): Boolean = {
    userService.addUser(email)
  }

  override def isEmailRegistered(email: String): Boolean = {
    userService.isUserRegistered(email)
  }


  override def getUserByEmail(email: String): Option[UiUser] = {
    userService.getUserByEmail(email).map{
      UiUser(_)
    }
  }

  override def setUserBundleFailed(uiUserBundle: UiUserBundle): Unit = {
    val c = uiUserBundle.uiTimetableConfig
    timetableConfigService.setTimetableConfigError(TimetableConfig(c.configId,c.userId,c.url,c.school,c.elementType,c.elementId,c.userName,c.password,c.error), true)
  }

  override def addTimetableConfig(userId: UUID, server: String, school: String, user: String, password: String, elementId: Int, elementType: Int): Unit = {
    timetableConfigService.addTimetableConfig(userId, checkUrl(server), school, elementType,elementId, user, password, false)
  }

  private def checkUrl(url: String): String = {
    if(!url.startsWith("http")){
      s"https://${url}"
    }else{
      url
    }
  }

  override def addUserPushNotification(userId: UUID, pushId: String): Unit = {
    userNotificationService.addNotification(UserNotification(UUID.randomUUID(), userId, UserNotification.TYPE_PUSH, pushId))
  }
}
