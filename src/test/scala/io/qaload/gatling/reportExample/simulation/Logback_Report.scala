package io.qaload.gatling.reportExample.simulation

import io.gatling.core.Predef.{exec, _}
import io.gatling.core.structure.ScenarioBuilder
import io.qaload.gatling.reportExample.setting.Protocol
import io.qaload.gatling.util.UtilsFeed

import scala.language.postfixOps._
import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging
import io.gatling.http.Predef.http

class Logback_Report  extends Simulation with StrictLogging {

  def logbackScenario(name: String): ScenarioBuilder = scenario(name)
    .exec(
      http("/ (GET)").get("/")
    )
    .exec {
      session =>
        val test = session("test").asOption[Int]
        if(test.isDefined)
          session.set("test", test.get + 1)
        else
          session.set("test", 0)
    }
    .exec {
      session =>
        val test = session("test").asOption[Int]
        logger.error(s"val1;val2;${test}")
        session
    }

  setUp(
    logbackScenario("logbackScenario")
      .inject(
        atOnceUsers(100)             // 0
      ).protocols(Protocol.httpConf)
  )
}
