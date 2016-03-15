package org.victorp.akka.samples.pubsub

/**
 * @author victorp
 */
sealed abstract class LogEvent(val level:LogLevel){
  val channel = s"${LogChannel.ROOT}${level.name}"
  val message:String
}

case class InfoEvent(message:String) extends LogEvent(LogLevel.INFO)
case class WarnEvent(message:String) extends LogEvent(LogLevel.WARN)
case class ErrorEvent(message:String) extends LogEvent(LogLevel.ERROR)
case class DebugEvent(message:String) extends LogEvent(LogLevel.DEBUG)
case class TraceEvent(message:String) extends LogEvent(LogLevel.TRACE)


object LogEvent{
  def of(level:LogLevel,msg:String):LogEvent = level match {
    case LogLevel.INFO => InfoEvent(msg)
    case LogLevel.WARN => WarnEvent(msg)
    case LogLevel.ERROR => ErrorEvent(msg)
    case LogLevel.DEBUG => DebugEvent(msg)
    case LogLevel.TRACE => TraceEvent(msg)
  }
}


