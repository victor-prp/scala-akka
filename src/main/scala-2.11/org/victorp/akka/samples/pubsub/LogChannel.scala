package org.victorp.akka.samples.pubsub

/**
 * @author victorp
 */
object LogChannel {
  val ROOT = "LOG/"

  def of(level:LogLevel)={
    s"$ROOT${level.name}"
  }

}
