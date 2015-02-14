package models.user

import com.mohiva.play.silhouette.core.LoginInfo
import core.dao.BaseDocumentDao
import core.db.DBQueryBuilder
import models.user.User._
import play.api.libs.json.Json
import reactivemongo.api.indexes.IndexType
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
class UserDAOImplBase extends UserDAO with BaseDocumentDao[User]{


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

  override def ensureIndexes: Future[List[Boolean]] = {
    for {
      usernameIndex <- ensureIndex(List(("username", IndexType.Ascending)), unique = true)
      emailIndex <- ensureIndex(List(("email", IndexType.Ascending)), unique = true)
    } yield {
      List(usernameIndex, emailIndex)
    }
  }

  override def findAll(): Future[List[User]] = find()

  override def countFollowers(userId: String): Future[Int] = {
    count(DBQueryBuilder.in("following", Seq(userId)))
  }

  override def findUsersByIds(authorsIds: List[String]): Future[List[User]] = {
    find(DBQueryBuilder.byIds(authorsIds))
  }

}
