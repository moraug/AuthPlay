package model

object UserDao {
  private var usersLocation: String = "/users"

  def validateUser(usename: String, password: String): Boolean = {
    //TODO: read file and check that credentials are there
    false
  }
}
