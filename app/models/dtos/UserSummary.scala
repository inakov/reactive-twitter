package models.dtos

import play.api.libs.json.Json

/**
 * Created by inakov on 1/23/15.
 */
case class UserSummary(id: String,
                       username: String,
                       name: Option[String],
                       avatarURL: Option[String],
                       biography: Option[String],
                       verified: Option[Boolean],
                       tweets: Integer,
                       following: Integer,
                       followers: Integer)

object UserSummary{
  implicit val userSummaryFormat = Json.format[UserSummary]
}
