package core.db
import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin

import reactivemongo.bson.{BSONObjectID, BSONValue}

import core.helpers.ContextHelper
/**
 * Created by inakov on 12.01.15.
 */
trait MongoHelper extends ContextHelper{
  lazy val db = ReactiveMongoPlugin.db
}

object MongoHelper extends MongoHelper {
  def identify(bson: BSONValue) = bson.asInstanceOf[BSONObjectID].stringify
}
