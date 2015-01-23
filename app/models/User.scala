package models

import com.mohiva.play.silhouette.core.{LoginInfo, Identity}
import core.models.IdentifiableModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

case class User(
                 override  var _id: Option[BSONObjectID],
                 loginInfo: LoginInfo,
                 avatarURL: Option[String],
                 name: Option[String],
                 username: String,
                 email: String,
                 created: DateTime,
                 biography: Option[String] = None,
                 following: Set[String],
                 verified: Option[Boolean]
                 ) extends Identity with IdentifiableModel

object User {
  import play.modules.reactivemongo.json.BSONFormats._
  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val userFormat = Json.format[User]
}
