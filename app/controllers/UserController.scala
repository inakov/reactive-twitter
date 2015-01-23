package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import models.User
import models.services.UserService
import play.api.libs.json.Json
import play.api.mvc.BodyParsers.parse
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by inakov on 1/23/15.
 */
class UserController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator],
                                val userService: UserService)
  extends Silhouette[User, CachedCookieAuthenticator] {

  def loadUserSummary(username: String) = SecuredAction.async{implicit request => {
      userService.loadUserSummary(username).map(userSummary => Ok(Json.toJson(userSummary)))
    }
  }

}
