package models

import com.mohiva.play.silhouette.core.LoginInfo
import core.models.IdentifiableModel
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._
import com.mohiva.play.silhouette.core.providers.PasswordInfo

/**
 * Created by inakov on 13.01.15.
 */
case class Password(
                     override var _id: Option[BSONObjectID],
                     loginInfo: LoginInfo,
                     passwordInfo: PasswordInfo
                     ) extends IdentifiableModel
object Password{
  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val passwordFormat = Json.format[Password]
}