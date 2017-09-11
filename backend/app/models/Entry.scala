
package models

import java.text._
import java.util._
import play.api.libs.json._
import scala.collection.mutable


object Entry {
  val allEntries = mutable.Set[Entry]()

  implicit val writes = new Writes[Entry] {

    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    val timeZone = TimeZone.getTimeZone("UTC")
    dateFormat.setTimeZone(timeZone)

    def writes(entry: Entry) = Json.obj(
      "id" -> entry.id,
      "date" -> dateFormat.format(entry.date),
      "description" -> entry.description,
      "amount" -> entry.amount,
      "category" -> entry.category.mkString("/")
    )
  }
}

case class Entry(id: String, date: Date, description: String, amount: Double, category: Seq[String]) {
}

