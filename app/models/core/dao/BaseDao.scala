package core.dao

import scala.concurrent.Future

import play.api.Logger

import reactivemongo.core.commands.LastError
import reactivemongo.core.errors.DatabaseException

import core.db.MongoHelper
import core.exceptions._

/* Implicits */
import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by inakov on 12.01.15.
 */
trait BaseDao extends MongoHelper {

  val collectionName: String

  def ensureIndexes: Future[List[Boolean]]

  def Recover[S](operation: Future[LastError])(success: => S): Future[Either[ServiceException, S]] = {
    operation.map {
      lastError => lastError.inError match {
        case true => {
          Logger.error(s"DB operation did not perform successfully: [lastError=$lastError]")
          Left(DBServiceException(lastError))
        }
        case false => {
          Right(success)
        }
      }
    } recover {
      case exception =>
        Logger.error(s"DB operation failed: [message=${exception.getMessage}]")

        //TODO: better failure handling here
        val handling: Option[Either[ServiceException, S]] = exception match {
          case e: DatabaseException => {
            e.code.map(code => {
              Logger.error(s"DatabaseException: [code=${code}, isNotAPrimaryError=${e.isNotAPrimaryError}]")
              code match {
                case 10148 => {
                  Left(OperationNotAllowedException("", nestedException = e))
                }
                case 11000 => {
                  Left(DuplicateResourceException(nestedException = e))
                }
              }
            })
          }
        }
        handling.getOrElse(Left(UnexpectedServiceException(exception.getMessage, nestedException = exception)))
    }
  }
}
