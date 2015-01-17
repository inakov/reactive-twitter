package models.daos

import core.exceptions.ServiceException
import models.User
import com.mohiva.play.silhouette.core.LoginInfo
import play.api.libs.json.{Writes, JsObject}
import reactivemongo.bson.BSONObjectID
import scala.concurrent.Future

/**
 * Give access to the user object.
 */
trait UserDAO {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo): Future[Option[User]]

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: BSONObjectID): Future[Option[User]]

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]

  def findAll(): Future[List[User]]

  def update(id: String, query: JsObject): Future[Either[ServiceException, JsObject]]
  def pull[S](id: String, field: String, query: S)(implicit writer: Writes[S]): Future[Either[ServiceException, Boolean]]
}
