package core.dao

import core.models.IdentifiableModel
import org.joda.time.DateTime
import play.modules.reactivemongo.json.BSONFormats
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import scala.concurrent.Future

import play.api.Logger
import play.api.libs.json._
import play.modules.reactivemongo.json.collection.JSONCollection

import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson._

import core.db.DBQueryBuilder
import core.exceptions.ServiceException
/**
 * Created by inakov on 12.01.15.
 */
trait BaseDocumentDao[M <: IdentifiableModel] extends BaseDao with DocumentDao[M]{

  lazy val collection = db.collection[JSONCollection](collectionName)

  def insert(document: M)(implicit writer: Writes[M]): Future[Either[ServiceException, M]] = {
    document._id = Some(BSONObjectID.generate)
    Logger.debug(s"Inserting document: [collection=$collectionName, data=$document]")
    Recover(collection.insert(document)) {
      document
    }
  }

  def find(query: JsObject = Json.obj())(implicit reader: Reads[M]): Future[List[M]] = {
    Logger.debug(s"Finding documents: [collection=$collectionName, query=$query]")
    collection.find(query).cursor[M].collect[List]()
  }

  def findWithOptions(query: JsObject = Json.obj(), queryOpts: QueryOpts)(implicit reader: Reads[M]): Future[List[M]] = {
    Logger.debug(s"Finding documents: [collection=$collectionName, query=$query]")
    collection.find(query).options(queryOpts).cursor[M].collect[List]()
  }

  def findById(id: String)(implicit reader: Reads[M]): Future[Option[M]] = findOne(DBQueryBuilder.id(id))

  def findById(id: BSONObjectID)(implicit reader: Reads[M]): Future[Option[M]] = findOne(DBQueryBuilder.id(id))

  def findOne(query: JsObject)(implicit reader: Reads[M]): Future[Option[M]] = {
    Logger.debug(s"Finding one: [collection=$collectionName, query=$query]")
    collection.find(query).one[M]
  }

  def update(id: String, document: M)(implicit writer: Writes[M]): Future[Either[ServiceException, M]] = {
    Logger.debug(s"Updating document: [collection=$collectionName, id=$id, document=$document]")
    Recover(collection.update(DBQueryBuilder.id(id), DBQueryBuilder.set(document))) {
      document
    }
  }

  def update(id: String, query: JsObject): Future[Either[ServiceException, JsObject]] = {
    val data = updated(query)
    Logger.debug(s"Updating by query: [collection=$collectionName, id=$id, query=$data]")
    Recover(collection.update(DBQueryBuilder.id(id), data)) {
      data
    }
  }

  def push[S](id: String, field: String, data: S)(implicit writer: Writes[S]): Future[Either[ServiceException, S]] = {
    Logger.debug(s"Pushing to document: [collection=$collectionName, id=$id, field=$field data=$data]")
    Recover(collection.update(DBQueryBuilder.id(id), DBQueryBuilder.push(field, data)
    )) {
      data
    }
  }

  def pull[S](id: String, field: String, query: S)(implicit writer: Writes[S]): Future[Either[ServiceException, Boolean]] = {
    Logger.debug(s"Pulling from document: [collection=$collectionName, id=$id, field=$field query=$query]")
    Recover(collection.update(DBQueryBuilder.id(id), DBQueryBuilder.pull(field, query))) {
      true
    }
  }

  def unset(id: String, field: String): Future[Either[ServiceException, Boolean]] = {
    Logger.debug(s"Unsetting from document: [collection=$collectionName, id=$id, field=$field]")
    Recover(collection.update(DBQueryBuilder.id(id), DBQueryBuilder.unset(field))) {
      true
    }
  }

  def remove(id: String): Future[Either[ServiceException, Boolean]] = remove(BSONObjectID(id))

  def remove(id: BSONObjectID): Future[Either[ServiceException, Boolean]] = {
    Logger.debug(s"Removing document: [collection=$collectionName, id=$id]")
    Recover(
      collection.remove(DBQueryBuilder.id(id))
    ) {
      true
    }
  }

  def remove(query: JsObject, firstMatchOnly: Boolean = false): Future[Either[ServiceException, Boolean]] = {
    Logger.debug(s"Removing document(s): [collection=$collectionName, firstMatchOnly=$firstMatchOnly, query=$query]")
    Recover(
      collection.remove(query, firstMatchOnly = firstMatchOnly)
    ) {
      true
    }
  }

  def updated(data: JsObject) = {
    data.validate((__ \ '$set).json.update(
      __.read[JsObject]
    )).fold(
        error => data,
        success => success
      )
  }

  def count(query: JsObject): Future[Int] = {
    val BSONQuery = BSONFormats.toBSON(query).get.asInstanceOf[BSONDocument]
    collection.db.command(
      Count(
        collection.name,
        Some(BSONQuery)
      )
    )
  }

  def ensureIndex(
                   key: List[(String, IndexType)],
                   name: Option[String] = None,
                   unique: Boolean = false,
                   background: Boolean = false,
                   dropDups: Boolean = false,
                   sparse: Boolean = false,
                   version: Option[Int] = None,
                   options: BSONDocument = BSONDocument()) = {
    val index = Index(key, name, unique, background, dropDups, sparse, version, options)
    Logger.info(s"Ensuring index: $index")
    collection.indexesManager.ensure(index)
  }
}
