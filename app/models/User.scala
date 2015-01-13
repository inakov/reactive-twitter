package models

import com.mohiva.play.silhouette.core.{LoginInfo, Identity}
import core.models.IdentifiableModel
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

case class User(
                 override  var _id: Option[BSONObjectID],
                 loginInfo: LoginInfo,
                 firstName: Option[String],
                 lastName: Option[String],
                 fullName: Option[String],
                 email: Option[String],
                 avatarURL: Option[String]
                 ) extends Identity with IdentifiableModel

object User {
  import play.modules.reactivemongo.json.BSONFormats._
  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val userFormat = Json.format[User]
}
