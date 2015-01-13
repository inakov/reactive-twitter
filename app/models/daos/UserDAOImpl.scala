package models.daos

import models.User
import com.mohiva.play.silhouette.core.LoginInfo

import scala.collection.mutable
import java.util.UUID
import UserDAOImpl._
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.BSONFormats._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.BSONObjectID
import play.api.Play.current
import models.User._

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {

  private def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("users")

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo) = {
    collection.find(
      Json.obj("loginInfo" ->
        Json.obj("providerID" ->loginInfo.providerID, "providerKey"->loginInfo.providerKey))).one[User]
  }

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: BSONObjectID) = {
    collection.find(Json.obj("userId" -> userID.stringify)).one[User]
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    collection.save(user).map {
      case ok if ok.ok =>
        user
      case error => throw new RuntimeException(error.message)
    }
  }
}

/**
 * The companion object.
 */
object UserDAOImpl {


  /**
   * The list of users.
   */
  val users: mutable.HashMap[UUID, User] = mutable.HashMap()
}
