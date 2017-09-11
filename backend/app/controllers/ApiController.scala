package controllers

import java.io._
import javax.inject._
import models._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.io._
import services._

case class ImportRequest(dataFormat: String, file: String)
object ImportRequest {
  val form = Form(
    mapping(
      "dataFormat" -> nonEmptyText,
      "file" -> text
    )(ImportRequest.apply)(ImportRequest.unapply)
  )
}

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getEntries() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.obj(
      "entries" -> Entry.allEntries.toSeq.sortWith((e1: Entry, e2: Entry) =>
        {(e1.date compareTo e2.date) > 0})
    ))
  }

  def importEntries() = Action { implicit request: Request[AnyContent] =>
    ImportRequest.form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(Json.obj(
          "success" -> false,
          "errors" -> formWithErrors.errors.map(error =>
            Json.obj(
              "key" -> error.key,
              "messages" -> error.messages
            )
          )
        ))
      },
      importRequestData => {
        
        importRequestData.dataFormat match {
          case "wellsfargo" => {
            if (ImportEntries.wellsFargo(importRequestData.file)) {
              Ok(Json.obj("success" -> true))
            } else {
              BadRequest(Json.obj(
                "success" -> false,
                "errors" -> Seq(Json.obj(
                  "key" -> "file",
                  "messages" -> Seq("cannot interpret file as Wells Fargo format")
                ))
              ))
            }
          }
          case "venmo" => {
            if (ImportEntries.venmo(importRequestData.file)) {
              Ok(Json.obj("success" -> true))
            } else {
              BadRequest(Json.obj(
                "success" -> false,
                "errors" -> Seq(Json.obj(
                  "key" -> "file",
                  "messages" -> Seq("cannot interpret file as Venmo format")
                ))
              ))
            }
          }
          case "splitwise" => {
            if (ImportEntries.splitwise(importRequestData.file)) {
              Ok(Json.obj("success" -> true))
            } else {
              BadRequest(Json.obj(
                "success" -> false,
                "errors" -> Seq(Json.obj(
                  "key" -> "file",
                  "messages" -> Seq("cannot interpret file as Splitwise format")
                ))
              ))
            }
          }
          case _ => BadRequest(Json.obj(
              "success" -> false,
              "errors" -> Seq(Json.obj(
                "key" -> "dataFormat",
                "messages" -> Seq("unknown data format")
              ))
            ))
        }
    });
  }
}
