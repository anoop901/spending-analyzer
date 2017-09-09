package controllers

import java.io._
import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.io._
import services.CsvParser

case class ImportRequest(dataFormat: String, file: String)
object ImportRequest {
	val form = Form(
	  mapping(
	    "dataFormat" -> text,
	    "file" -> text
	  )(ImportRequest.apply)(ImportRequest.unapply)
	)
}

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getEntries() = Action { implicit request: Request[AnyContent] =>
    val source = new java.io.FileInputStream("data.json")
    Ok(Json.parse(source))
  }

  def importEntries() = Action { implicit request: Request[AnyContent] =>
  	ImportRequest.form.bindFromRequest.fold(
  		formWithErrors => {
  			BadRequest("Invalid form data: " + formWithErrors)
  		},
  		importRequestData => {
  			println(s"Format: ${importRequestData.dataFormat}")
  			println(CsvParser.parse(importRequestData.file))
  			Ok("")
		});
  }
}
