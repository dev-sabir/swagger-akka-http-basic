package com.example.akka.wlcm

import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import com.example.akka.DefaultJsonFormats
import com.thirdnormal.spark.CorsSupport
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.{Consumes, GET, POST, Path, Produces}
import pl.iterators.kebs.json.KebsSpray
import spray.json.RootJsonFormat

import scala.concurrent.duration.DurationInt

@Path("/wlcm")
object WlcmService extends Directives with DefaultJsonFormats with KebsSpray {

  case class Wlcm(greetMessage: String)

  implicit val timeout: Timeout = Timeout(2.seconds)
  implicit val wlcmFormat: RootJsonFormat[Wlcm] = jsonFormatN[Wlcm]

  val route: Route = wl

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Welcome", description = "greet message",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Greeting",
        content = Array(new Content(schema = new Schema(implementation = classOf[Wlcm])))),
      new ApiResponse(responseCode = "500", description = "Internal Server Error"))
  )
  def wl: Route =
    path("wlcm") {
      get {
          complete("Welcome to Scala World")
      }
    }

}