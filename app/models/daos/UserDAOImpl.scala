package models.daos

import core.dao.DocumentDao
import models.User
import com.mohiva.play.silhouette.core.LoginInfo

import scala.concurrent.Future
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import models.User._

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO with DocumentDao[User]{

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo) = {
    findOne(Json.obj("loginInfo" ->
      Json.obj("providerID" ->loginInfo.providerID, "providerKey"->loginInfo.providerKey)))
  }

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: BSONObjectID) = {
    find(userID)
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    insert(user).map{
      result => result match{
        case Right(document) => document
        case Left(ex) => throw ex
      }
    }
  }

  override val collectionName: String = "users"
  override def ensureIndexes: Future[List[Boolean]] = Future.successful(Nil)
}
