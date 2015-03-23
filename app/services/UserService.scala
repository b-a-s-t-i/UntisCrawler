package services

import java.util.UUID

import model.User
import org.joda.time.DateTime
import play.api.Logger
import scaldi.{Injector, Injectable}
import storage.UserStorage
import play.api.db.slick.DB
import play.api.Play.current

trait UserService{
  def addUser(email: String): Boolean
  def getUserByEmail(email: String): Option[User]
  def getUserById(id: UUID): Option[User]
  def isUserRegistered(email: String): Boolean
  def getAllUser(): List[User]
}

class UserServiceImpl(implicit inj: Injector) extends UserService with Injectable {

  val storage = inject[UserStorage]

  override def addUser(email: String): Boolean = {
    DB.withTransaction { implicit session =>
      if(!storage.existsUserWithEmail(email)){
        val u = User(email, UUID.randomUUID(), DateTime.now())
        Logger.info(u.toString)
        storage.addUser(u)
        true
      }else{
        false
      }
    }
  }

  override def getUserByEmail(email: String): Option[User] = {
    DB.withSession{ implicit  session =>
      storage.getUserByEmail(email)
    }
  }

  override def getAllUser(): List[User] = {
    DB.withSession { implicit session =>
      storage.getAllUser
    }
  }

  override def isUserRegistered(email: String): Boolean = {
    DB.withSession{ implicit session =>
      storage.existsUserWithEmail(email)
    }
  }

  override def getUserById(id: UUID): Option[User] = {
    DB.withSession{ implicit session =>
      storage.getUserById(id)
    }
  }
}
