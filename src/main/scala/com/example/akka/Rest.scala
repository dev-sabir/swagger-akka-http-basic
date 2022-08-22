package com.example.akka

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.example.akka.hello.{HelloActor, HelloService}
import com.example.akka.swagger.SwaggerDocService
import com.example.akka.wlcm.WlcmService
import com.thirdnormal.spark.CorsSupport

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

object Rest extends App with RouteConcatenation with CorsSupport {
  implicit val system: ActorSystem = ActorSystem("akka-http-sample")
  sys.addShutdownHook(system.terminate())

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val hello = system.actorOf(Props[HelloActor])

  val routes =
     (
      new HelloService(hello).route ~
        WlcmService.route ~
      SwaggerDocService.routes)

  val f = for {
    bindingFuture <- Http().newServerAt("0.0.0.0", 12345).bind(corsHandler(routes))
    waitOnFuture  <- Future.never
  } yield waitOnFuture

  Await.ready(f, Duration.Inf)
}
