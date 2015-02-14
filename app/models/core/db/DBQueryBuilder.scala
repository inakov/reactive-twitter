package core.db

import play.api.libs.json.{Writes, Json, JsObject}

import reactivemongo.bson.BSONObjectID

/* Implicits */
import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by inakov on 12.01.15.
 */
object DBQueryBuilder {

  def id(objectId: String): JsObject = id(BSONObjectID(objectId))

  def id(objectId: BSONObjectID): JsObject = Json.obj("_id" -> objectId)

  def byIds(objectIds: List[String]): JsObject = in("_id", objectIds.map(BSONObjectID(_)))

  def set(field: String, data: JsObject): JsObject = set(Json.obj(field -> data))

  def set[T](field: String, data: T)(implicit writer: Writes[T]): JsObject = set(Json.obj(field -> data))

  def set(data: JsObject): JsObject = Json.obj("$set" -> data)

  def set[T](data: T)(implicit writer: Writes[T]): JsObject = Json.obj("$set" -> data)

  def push[T](field: String, data: T)(implicit writer: Writes[T]): JsObject = Json.obj("$push" -> Json.obj(field -> data))

  def pull[T](field: String, query: T)(implicit writer: Writes[T]): JsObject = Json.obj("$pull" -> Json.obj(field -> query))

  def unset(field: String): JsObject = Json.obj("$unset" -> Json.obj(field -> 1))

  def inc(field: String, amount: Int) = Json.obj("$inc" -> Json.obj(field -> amount))

  def or(criterias: JsObject*): JsObject = Json.obj("$or" -> criterias)

  def gt[T](field: String, value: T)(implicit writer: Writes[T]) = Json.obj(field -> Json.obj("$gt" -> value))

  def lt[T](field: String, value: T)(implicit writer: Writes[T]) = Json.obj(field -> Json.obj("$lt" -> value))

  def in[T](field: String, values: Seq[T])(implicit writer: Writes[T]) = Json.obj(field -> Json.obj("$in" -> values))

  def all[T](field: String, values: Seq[T])(implicit writer: Writes[T]) = Json.obj(field -> Json.obj("$all" -> values))

  def query[T](query: T)(implicit writer: Writes[T]): JsObject = Json.obj("$query" -> query)

  def orderBy[T](query: T)(implicit writer: Writes[T]): JsObject = Json.obj("$orderby" -> query)
}
