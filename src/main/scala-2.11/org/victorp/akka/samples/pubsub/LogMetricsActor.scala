package org.victorp.akka.samples.pubsub

import akka.actor.Actor

/**
 * @author victorp
 */
class LogMetricsActor(val eventBus:LogEventBus, logLevel:LogLevel) extends Actor{

  var currentLevel = logLevel

  override def preStart(): Unit = {
    //subscribe for all log events
    eventBus.subscribe(self,LogChannel.of(currentLevel))
  }

  override def receive: Receive = {
    case logEvent:LogEvent =>
      println(s"aggregating metrics for -> ${logEvent.level.name} ${logEvent.message}")

    case updateLogLevel : UpdateLogLevel =>
      if (eventBus.subscribe(self,LogChannel.of(updateLogLevel.level))){
        eventBus.unsubscribe(self,LogChannel.of(currentLevel))
        currentLevel = updateLogLevel.level
      }
  }
}