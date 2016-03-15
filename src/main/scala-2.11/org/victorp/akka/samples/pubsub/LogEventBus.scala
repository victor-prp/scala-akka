package org.victorp.akka.samples.pubsub

import akka.actor.ActorRef
import akka.event.{SubchannelClassification, EventBus}
import akka.util.Subclassification

/**
 * @author victorp
 */
class LogEventBus extends EventBus with SubchannelClassification {
  type Classifier = String
  type Event = LogEvent
  type Subscriber = ActorRef

  override protected implicit def subclassification = new Subclassification[String] {
    def isEqual(
                 subscribedToClassifier: String,
                 eventClassifier: String): Boolean = {

      subscribedToClassifier.equals(eventClassifier)
    }

    def isSubclass(
                    subscribedToClassifier: String,
                    eventClassifier: String): Boolean = {
      subscribedToClassifier.startsWith(eventClassifier)
    }
  }

  override protected def publish(event: LogEvent, subscriber: ActorRef): Unit = {
    subscriber ! event
  }

  override protected def classify(event: LogEvent): String = {
    event.channel
  }
}
