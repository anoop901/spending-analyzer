
package models

import java.util._
import scala.collection.mutable

object Entry {
  val allEntries = mutable.SortedSet[Entry]()
}

case class Entry(date: Date, description: String, amount: Double, category: Seq[String]) extends Ordered[Entry] {
  override def compare(that: Entry) = this.date compareTo that.date
}
