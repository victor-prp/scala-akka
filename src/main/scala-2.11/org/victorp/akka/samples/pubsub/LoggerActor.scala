package org.victorp.akka.samples.pubsub

import akka.actor.Actor

/**
 * @author victorp
 */
class LoggerActor(val eventBus:LogEventBus, logLevel:LogLevel) extends Actor{

  var currentLevel = logLevel

  override def preStart(): Unit = {
    //subscribe for all log events
    eventBus.subscribe(self,LogChannel.ROOT)
  }

  override def receive: Receive = {
    case logEvent:LogEvent => if (logEvent.level.priority <= currentLevel.priority) {
      println(s"printing to log -> ${logEvent.level.name} ${logEvent.message}")
    }
  }
}
