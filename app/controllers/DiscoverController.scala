package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import models.User
import models.services.UserService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by inakov on 15.01.15.
 */
class DiscoverController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator],
                                    val userService: UserService)
  extends Silhouette[User, CachedCookieAuthenticator] {

  def discover = SecuredAction.async{ implicit request =>{
    userService.findAll().map(users => users.filter(_._id != request.identity._id)).map{
        users => Ok(views.html.discover(users, request.identity))
      }
    }
  }

}
