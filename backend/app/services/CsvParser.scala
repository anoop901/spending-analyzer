
package services

import com.univocity.parsers.csv._
import java.io._
import scala.collection.JavaConverters._

object CsvParser {
  def parse(s: String): Seq[Seq[String]] = {
    val settings = new CsvParserSettings()
    val parser = new CsvParser(settings)
    val reader = new StringReader(s)

    parser.parseAll(reader).asScala.toIndexedSeq.map(_.toIndexedSeq)
  }
}
