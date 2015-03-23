package storage

import java.util.UUID

import model.User
import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import com.github.tototoshi.slick.JdbcJodaSupport._

class UserTable(tag: Tag) extends Table[User](tag, User.TABLE){

  def userId = column[UUID](User.ID_KEY, O.PrimaryKey)
  def email = column[String](User.EMAIL_KEY, O.NotNull)
  def timeStampCreate = column[DateTime](User.TIMESTAMP_CREATED_KEY, O.NotNull)

  override def * = (email, userId, timeStampCreate) <> ((User.apply _).tupled, User.unapply)

  def idx = index(User.EMAIL_IDX, email, unique = true)
}

class UserStorage {


  val table = TableQuery[UserTable]

  def addUser(user: User)(implicit session: Session) = {
    table.insert(user)
  }

  def getUserByEmail(email: String)(implicit session: Session): Option[User] = {
    table.filter(_.email === email).firstOption
  }

  def existsUserWithEmail(email: String)(implicit session: Session): Boolean = {
    getUserByEmail(email).isDefined
  }

  def getAllUser()(implicit session: Session): List[User] = {
    table.list
  }

  def updateUser(updatedUser: User)(implicit session: Session) = {
    table.filter(_.userId === updatedUser.userId).update(updatedUser)
  }

  def getUserById(uuid: UUID)(implicit session: Session): Option[User] = {
    table.filter(_.userId === uuid).firstOption
  }
}
