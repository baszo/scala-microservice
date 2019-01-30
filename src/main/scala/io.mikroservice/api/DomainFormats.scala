package io.mikroservice.api

import io.mikroservice.domain.BearId
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

/**
  * @author plutecki
  */

object DomainFormats {

  val all = List(
    BearIdSerializer
  )

  object BearIdSerializer extends CustomSerializer[BearId](_ => ( {
    case JString(value) => BearId(value)
  }, {
    case BearId(value) => JString(value)
  }))

}
