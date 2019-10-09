package model

import scala.io.Source

object UserDao {
  private val usersLocation: String = "users"

  def validateUser(username: String, password: String): Boolean = {
    var result = false
    val source = Source.fromResource(usersLocation)
    val lines = source.getLines
    for (line <- lines) {
      val user = line.split(":")(0)
      val pwd = line.split(":")(1)
      if (user.equals(username) && pwd.equals(password)) {
        result = true
      }
    }
    result
  }
}
