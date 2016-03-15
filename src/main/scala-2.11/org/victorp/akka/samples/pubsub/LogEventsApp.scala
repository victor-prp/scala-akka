package org.victorp.akka.samples.pubsub

import akka.actor.{Props, ActorSystem}

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
/**
 * @author victorp
 */
object LogEventsApp extends App{

  val system = ActorSystem("logSystem")

  val logEventBus = new LogEventBus
  val loggerActor = system.actorOf(Props(new LoggerActor(logEventBus, LogLevel.INFO)),"loggerActor")
  val logMetricsActor = system.actorOf(Props(new LogMetricsActor(logEventBus, LogLevel.TRACE)),"logMetricsActor")


  Future {
    val range = 1 to 2000
    for (i <- range) {
      val logLevel = LogLevel.of(i % 5)
      logEventBus.publish(LogEvent.of(logLevel, s"log message $i"))
    }
  }
  Thread.sleep(10)

  logMetricsActor ! UpdateLogLevel(LogLevel.INFO)

  Thread.sleep(1000)
  System.exit(0)
}
