package models.user

import javax.inject.Inject

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.providers.CommonSocialProfile
import com.mohiva.play.silhouette.core.services.AuthInfo
import models.tweet.TweetService
import models.user.{UserDAO, UserSummary}
import org.joda.time.DateTime
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import scala.util.Random

/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 */
class UserServiceImpl @Inject() (userDAO: UserDAO, tweetService: TweetService) extends UserService {

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
          following = Set(),
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

  override def loadUserSummary(username: String): Future[UserSummary] = {
    for {
      user <- userDAO.findOne(Json.obj("username" -> username))
      countTweets <- tweetService.countTweets(user.get.identify)
      countFollowers <- userDAO.countFollowers(user.get.identify)
    } yield UserSummary(user.get.identify,
      user.get.username,
      user.get.name,
      user.get.avatarURL.getOrElse("/assets/images/silhouette.png"),
      user.get.biography,
      user.get.verified,
      countTweets,
      user.get.following.size,
      countFollowers)
  }

  override def discoverThreeUser(userId: String): Future[List[User]] = {
    val userCount = userDAO.count(Json.obj("_id" -> Json.obj("$ne" -> BSONObjectID(userId))))

    userCount.flatMap{count => {
      val skip = Random.nextInt(count-3)
      userDAO.findWithOptions(Json.obj("_id" -> Json.obj("$ne" -> BSONObjectID(userId))), skip, 3)
      }
    }
  }
}
