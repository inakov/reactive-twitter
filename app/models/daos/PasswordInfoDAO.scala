package models.daos

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.providers.PasswordInfo
import com.mohiva.play.silhouette.contrib.daos.DelegableAuthInfoDAO
import core.dao.DocumentDao
import models.Password
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import scala.concurrent.Future
import models.Password._

/**
 * The DAO to store the password information.
 */
class PasswordInfoDAO extends DelegableAuthInfoDAO[PasswordInfo] with DocumentDao[Password]{

  /**
   * Saves the password info.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The password info to save.
   * @return The saved password info or None if the password info couldn't be saved.
   */
  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    insert(Password(Some(BSONObjectID.generate), loginInfo, authInfo)).map{
      result => result match{
        case Right(password) => password.passwordInfo
        case Left(ex) => throw ex
      }
    }
  }

  /**
   * Finds the password info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved password info or None if no password info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    findOne(
      Json.obj("loginInfo" ->
        Json.obj("providerID" ->loginInfo.providerID, "providerKey"->loginInfo.providerKey))).map{
      result => result match{
        case Some(password) => Some(password.passwordInfo)
        case None => None
      }
    }
  }

  override val collectionName: String = "passwords"
  override def ensureIndexes: Future[List[Boolean]] = Future.successful(Nil)
}
