
package services

import java.text._
import java.util._
import models._
import play.api.libs.json._

object ImportEntries {

  def wellsFargo(file: String): Boolean = {
    val csvEntries: Seq[Seq[String]] = CsvParser.parse(file)

    val dateFormat: SimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy")
    val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    dateFormat.setTimeZone(timeZone)

    if (csvEntries.forall(csvEntry => csvEntry.length == 5)) {

      csvEntries.foreach(csvEntry => {
        val entry = Entry(
          UUID.randomUUID.toString,
          dateFormat.parse(csvEntry(0)),
          csvEntry(4),
          csvEntry(1).toDouble,
          "uncategorized" :: Nil
        )

        Entry.allEntries += entry
      })

      true
    } else {
      false
    }
  }
  def venmo(file: String): Boolean = {
    val csvEntries: Seq[Seq[String]] = CsvParser.parse(file) drop 1

    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    dateFormat.setTimeZone(timeZone)

    if (csvEntries.forall(csvEntry => csvEntry.length == 11)) {

      csvEntries.map(csvEntry => {
        val amountAbs = csvEntry(7).filter(c => "0123456789." contains c).toDouble
        val amount = csvEntry(7)(0) match {
          case '+' => amountAbs
          case '-' => - amountAbs
          case _ => 0 // Todo: return false in this case
        }

        val entry = Entry(
          UUID.randomUUID.toString,
          dateFormat.parse(csvEntry(1)),
          if (csvEntry(2) == "Payment") csvEntry(4) else csvEntry(2),
          amount,
          "uncategorized" :: Nil
        )
        Entry.allEntries += entry
        true
      }).forall(x => x)
    } else {
      false
    }
  }

  def splitwise(file: String): Boolean = {
    val json = Json.parse(file)

    val userId = (json \ "user" \ "id").as[Int]

    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    dateFormat.setTimeZone(timeZone)


    (json \ "expenses").as[Seq[JsValue]].foreach(expenseJson => {
      val amount = (expenseJson \ "repayments").as[Seq[JsValue]].map(repaymentJson => {
        ((repaymentJson \ "from").as[Int], (repaymentJson \ "to").as[Int]) match {
          case (borrowerId, lenderId) if borrowerId == userId => -(repaymentJson \ "amount").as[String].toDouble
          case (borrowerId, lenderId) if lenderId == userId => (repaymentJson \ "amount").as[String].toDouble
          case _ => 0
        }
      }).sum

      val entry = Entry(
        UUID.randomUUID.toString,
        dateFormat.parse((expenseJson \ "date").as[String]),
        (expenseJson \ "description").as[String],
        amount,
        "uncategorized" :: Nil
      )
      Entry.allEntries += entry
    })

    true
  }
}