package models.authentication

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.providers.PasswordInfo
import core.models.IdentifiableModel
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by inakov on 13.01.15.
 */
case class Password(
                     override var _id: Option[BSONObjectID],
                     loginInfo: LoginInfo,
                     passwordInfo: PasswordInfo
                     ) extends IdentifiableModel
object Password{
  import play.modules.reactivemongo.json.BSONFormats._
  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val passwordFormat = Json.format[Password]
}