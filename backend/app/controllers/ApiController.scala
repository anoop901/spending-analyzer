package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.io._

import java.io._

import com.fasterxml.jackson.core.JsonParseException

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getEntries() = Action { implicit request: Request[AnyContent] =>
    val source = new java.io.FileInputStream("data.json")
    Ok(Json.parse(source))
  }
}
