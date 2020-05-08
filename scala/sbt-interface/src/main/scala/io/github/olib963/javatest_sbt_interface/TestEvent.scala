package io.github.olib963.javatest_sbt_interface

import sbt.testing._

object TestEvent {
  private def createEvent(taskDef: TaskDef, eventStatus: Status, error: Option[Throwable] = None): Event = new Event {
    override def fullyQualifiedName(): String = taskDef.fullyQualifiedName()
    override def fingerprint(): Fingerprint = taskDef.fingerprint()
    override def selector(): Selector = taskDef.selectors().head // TODO what if the number of selectors != 1?
    override def status(): Status = eventStatus
    override def throwable(): OptionalThrowable = error.fold(new OptionalThrowable())(new OptionalThrowable(_))
    override def duration(): Long = -1 // TODO do we want to add durations?
  }

  def apply(taskDef: TaskDef, eventStatus: Status): Event = createEvent(taskDef, eventStatus)
  def apply(taskDef: TaskDef, eventStatus: Status, error: Throwable): Event = createEvent(taskDef, eventStatus, Some(error))
}
