package core.models

import reactivemongo.bson.BSONObjectID

/**
 * Created by inakov on 12.01.15.
 */
trait IdentifiableModel {

  var _id: Option[BSONObjectID]

  def identify = _id.map(value => value.stringify).getOrElse("unknown")
}
