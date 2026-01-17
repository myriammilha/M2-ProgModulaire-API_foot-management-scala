package org.openapitools.server

import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Route
import org.openapitools.server.api.DefaultApi

import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.ActorMaterializer

class Controller(default: DefaultApi)(implicit system: ActorSystem, materializer: ActorMaterializer) {

    lazy val routes: Route = default.route 

    Http().bindAndHandle(routes, "0.0.0.0", 9000)
}