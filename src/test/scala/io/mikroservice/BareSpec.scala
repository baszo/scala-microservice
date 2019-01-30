package io.mikroservice

import akka.http.scaladsl.model.StatusCodes._
import io.mikroservice.core.BareEventpec

class BareSpec extends BareEventpec {


  "Some Service" should "be OK when correct data comes " in {
    val requestData = Map(
      "name" -> "test"
    )
    PostRequest("/create", requestData) ~> endpoint ~> check {
      status shouldBe Accepted
      val json = responseAsJson
      (json \ "data").extractOpt[String] shouldBe Some("324971")
    }
  }

}
