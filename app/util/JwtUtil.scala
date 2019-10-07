package util

import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtHeader, JwtJson}
import play.api.libs.json.Json

import scala.util.{Failure, Success}

class JwtUtil {

  val JwtSecretKey = "secretKey"
  val JwtSecretAlgo: JwtAlgorithm.HS256.type = JwtAlgorithm.HS256

  def createToken(payload: String): String = {
    val header = JwtHeader(JwtSecretAlgo, "JWT")
    val claim = JwtClaim(payload)
    JwtJson.encode(header, claim, JwtSecretKey)
  }

  def isValidToken(jwtToken: String): Boolean =
    JwtJson.isValid(jwtToken, JwtSecretKey, Seq(JwtSecretAlgo))

  def decodePayload(jwtToken: String): Option[String] = {
    val res = JwtJson.decode(jwtToken, JwtSecretKey, Seq(JwtSecretAlgo))
    res match {
      case Success(value) => Option(value.toJson)
      case Failure(exception) => None
    }
  }

}
