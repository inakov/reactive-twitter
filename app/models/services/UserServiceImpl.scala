package models.services

import javax.inject.Inject
import org.joda.time.DateTime
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.services.AuthInfo
import com.mohiva.play.silhouette.core.providers.CommonSocialProfile
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import scala.concurrent.Future
import models.daos.UserDAO
import models.User
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 */
class UserServiceImpl @Inject() (userDAO: UserDAO) extends UserService {

  /**
   * Retrieves a user that matches the specified login info.
   *
   * @param loginInfo The login info to retrieve a user.
   * @return The retrieved user or None if no user could be retrieved for the given login info.
   */
  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDAO.find(loginInfo)

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = userDAO.save(user)

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save[A <: AuthInfo](profile: CommonSocialProfile[A]) = {
    userDAO.find(profile.loginInfo).flatMap {
      case Some(user) => // Update user with profile
        userDAO.save(user.copy(
          name = profile.fullName,
          email = profile.email.getOrElse(""),
          avatarURL = profile.avatarURL
        ))
      case None => // Insert a new user
        userDAO.save(User(
          _id = Some(BSONObjectID.generate),
          loginInfo = profile.loginInfo,
          name = profile.fullName,
          username = "",
          email = profile.email.getOrElse(""),
          avatarURL = profile.avatarURL,
          created = DateTime.now,
          following = None,
          verified = None
        ))
    }
  }

  override def follow(followed: String, follower: String): Unit = {
    val query = Json.obj("$addToSet" -> Json.obj("following" -> followed))
    userDAO.update(follower, query)
  }

  override def unfollow(unfollowed: String, follower: String): Unit = {
    userDAO.pull(follower, "following", unfollowed)
  }

  override def discoverUser(userId: String): Future[List[User]] = {
    userDAO.find(Json.obj("_id" -> Json.obj("$ne" -> BSONObjectID(userId))))
  }
}
