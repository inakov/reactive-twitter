package core.helpers

import scala.concurrent.ExecutionContext

/**
 * Implicit contexts helper
 *
 * Created by inakov on 12.01.15.
 */
trait ContextHelper {

  implicit def ec: ExecutionContext = ExecutionContext.Implicits.global

}
