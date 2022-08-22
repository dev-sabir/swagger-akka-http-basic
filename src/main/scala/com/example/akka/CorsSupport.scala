package com.thirdnormal.spark

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server.{Directive0, Route}

trait CorsSupport {

  lazy val allowedOriginHeader = `Access-Control-Allow-Origin`.*

  private val OK = 200

  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preFlightRequestHandler ~ r
  }

  private def addAccessControlHeaders: Directive0 = {
    mapResponseHeaders { headers =>
      allowedOriginHeader +:
        `Access-Control-Allow-Credentials`(true) +:
        `Access-Control-Allow-Headers`("Token", "Content-Type", "X-Requested-With") +:
        headers
    }
  }

  private def preFlightRequestHandler: Route = options {
    complete(HttpResponse(OK).withHeaders(
      `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)
    )
    )
  }
}
