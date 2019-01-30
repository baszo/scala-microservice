package io.mikroservice

/**
  * @author plutecki
  */

trait Env {
  def toApplication: Application

  def bindHandler: () => Unit
}


object Main extends App {

  val environment = args.headOption match {
    case None => new DefaultEnv
    case Some("production2_0") => new Production20Env
    case other => throw new UnsupportedOperationException(s"Cannot start application from config: $other")
  }

  val application = environment.toApplication

  application.bind(environment.bindHandler)

}
