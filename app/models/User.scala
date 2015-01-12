package models

import com.mohiva.play.silhouette.core.{LoginInfo, Identity}
import org.jboss.netty.buffer._
import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.modules.reactivemongo.json.BSONFormats._

import reactivemongo.bson._

/**
 * The user object.
 *
 * @param userID The unique ID of the user.
 * @param loginInfo The linked login info.
 * @param firstName Maybe the first name of the authenticated user.
 * @param lastName Maybe the last name of the authenticated user.
 * @param fullName Maybe the full name of the authenticated user.
 * @param email Maybe the email of the authenticated provider.
 * @param avatarURL Maybe the avatar URL of the authenticated provider.
 */
case class User(
  userID: Option[BSONObjectID],
  loginInfo: LoginInfo,
  firstName: Option[String],
  lastName: Option[String],
  fullName: Option[String],
  email: Option[String],
  avatarURL: Option[String]
) extends Identity

// Turn off your mind, relax, and float downstream
// It is not dying...
object User {
  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val userFormat = Json.format[User]
  /*
  implicit object UserBSONReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User =
      User(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[LoginInfo]("loginInfo").get,
        doc.getAs[String]("firstName"),
        doc.getAs[String]("lastName"),
        doc.getAs[String]("fullName"),
        doc.getAs[String]("email"),
        doc.getAs[String]("avatarURL"))
  }
  implicit object UserBSONWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument =
      BSONDocument(
        "_id" -> user.userID.getOrElse(BSONObjectID.generate),
        "loginInfo" -> BSONDocument("providerKey" ->user.loginInfo.providerKey, "providerID" -> user.loginInfo.providerID),
        "firstName" -> user.firstName,
        "lastName" -> user.lastName,
        "fullName" -> user.fullName,
        "email" -> user.email,
        "avatarURL" -> user.avatarURL)
  }
  val form = Form(
    mapping(
      "id" -> optional(of[String] verifying pattern(
        """[a-fA-F0-9]{24}""".r,
        "constraint.objectId",
        "error.objectId")),
      "title" -> nonEmptyText,
      "content" -> text,
      "publisher" -> nonEmptyText,
      "creationDate" -> optional(of[Long]),
      "updateDate" -> optional(of[Long])) { (id, title, content, publisher, creationDate, updateDate) =>
      Article(
        id.map(new BSONObjectID(_)),
        title,
        content,
        publisher,
        creationDate.map(new DateTime(_)),
        updateDate.map(new DateTime(_)))
    } { article =>
      Some(
        (article.id.map(_.stringify),
          article.title,
          article.content,
          article.publisher,
          article.creationDate.map(_.getMillis),
          article.updateDate.map(_.getMillis)))
    })*/
}
