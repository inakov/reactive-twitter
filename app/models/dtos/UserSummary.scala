package models.dtos
import play.api.libs.json._

/**
 * Created by inakov on 1/23/15.
 */
case class UserSummary(id: String,
                       username: String,
                       name: Option[String],
                       avatarURL: String,
                       biography: Option[String],
                       verified: Option[Boolean],
                       tweets: Int,
                       following: Int,
                       followers: Int)

object UserSummary{
  implicit val userSummaryFormat = Json.format[UserSummary]
}
