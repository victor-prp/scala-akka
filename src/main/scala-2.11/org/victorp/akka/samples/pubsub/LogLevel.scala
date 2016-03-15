package org.victorp.akka.samples.pubsub

/**
 * @author victorp
 */
abstract sealed class LogLevel(val name:String, val priority:Int)

case class ERROR() extends LogLevel("ERROR",0)
case class WARN() extends LogLevel("WARN",1)
case class INFO() extends LogLevel("INFO",2)
case class DEBUG() extends LogLevel("INFO",3)
case class TRACE() extends LogLevel("TRACE",4)




object LogLevel{
  val ERROR = new ERROR()
  val WARN = new WARN()
  val INFO = new INFO()
  val DEBUG = new DEBUG()
  val TRACE = new TRACE()

  def of(priority:Int): LogLevel = priority match {
    case ERROR.priority => ERROR
    case WARN.priority => WARN
    case INFO.priority => INFO
    case DEBUG.priority => DEBUG
    case TRACE.priority => TRACE
  }


}



