package io.mikroservice.domain

import scala.util.control.NonFatal
import scala.util.matching.Regex

/**
  * @author plutecki
  */

case class ValidationException(errorKeys: List[String]) extends Exception(s"Validation errors occurred: $errorKeys.")

sealed trait ValidationResult

case object ValidationSuccess extends ValidationResult

case class ValidationFailure(errors: List[String]) extends ValidationResult

object ValidationFailure {
  def single(key: String) = throw ValidationException(List(key))
}

object Validate {

  def validateAndThrow(definitions: FieldConstrain*): Unit = {
    Validate(definitions: _*) match {
      case ValidationFailure(errors) => throw ValidationException(errors)
      case _ =>
    }
  }

  def apply(definitions: FieldConstrain*): ValidationResult = {
    val errors = definitions
      .map(field => field.validate)
      .collect {
        case Some(errorKey) => errorKey
      }

    if (errors.isEmpty) {
      ValidationSuccess
    } else {
      ValidationFailure(errors.toList)
    }
  }

  case class FieldConstrain(condition: Boolean, error: String) {
    def validate: Option[String] = try {
      if (condition) None else Some(error)
    } catch {
      case NonFatal(ex) => Some(error)
    }
  }

  implicit class StringExt(val value: String) extends AnyVal {
    def minLength(length: Int): Boolean = value.length >= length

    def maxLength(length: Int): Boolean = value.length <= length

    def matching(regex: Regex): Boolean = regex.findFirstMatchIn(value).isDefined
  }

  implicit class BooleanExt(val condition: Boolean) extends AnyVal {
    def ->(error: String) = FieldConstrain(condition, error)
  }

}
