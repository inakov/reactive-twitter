package models.dtos

import play.api.libs.json.Json

/**
 * Created by inakov on 1/23/15.
 */
case class UserSuggestion(userId: String,
                          avatarURL: Option[String],
                          name: Option[String],
                          username: String,
                          verified: Option[Boolean],
                          biography: Option[String] = None,
                          following: Boolean)

object UserSuggestion{
  implicit val userSuggestionFormat = Json.format[UserSuggestion]
}
