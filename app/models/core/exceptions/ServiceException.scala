package core.exceptions

/**
 * Created by inakov on 12.01.15.
 */
trait ServiceException extends Exception{
  val message: String
  val nestedException: Throwable
}
