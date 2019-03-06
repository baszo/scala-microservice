package io.mikroservice

import akka.http.scaladsl.model.StatusCodes._
import io.mikroservice.core.BareEventpec

class BareSpec extends BareEventpec {


  "Github Service" should "be OK when ask for existing user " in {
    val requestData = Map(
      "username" -> "baszo"
    )
    GetRequest("/user", requestData) ~> endpoint ~> check {
      status shouldBe OK
      val json = responseAsJson
      (json \ "login").extractOpt[String] shouldBe Some("baszo")
    }
  }

  it should "return 404 when no user found" in {
    val requestData = Map(
      "username" -> "hzJS5mdIFkctEnww"
    )
    GetRequest("/user", requestData) ~> endpoint ~> check {
      status shouldBe NotFound
    }
  }


}
