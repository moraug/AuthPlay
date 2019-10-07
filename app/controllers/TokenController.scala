package controllers

import javax.inject._
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._
import util.JwtUtil
import model.UserDao

case class TokenFormInput(username: String, password: String)

@Singleton
class TokenController @Inject()(
                                 cc: MessagesControllerComponents,
                                 util: JwtUtil
                               ) extends MessagesAbstractController(cc) {
  private val formToken: Form[TokenFormInput] = {
    import play.api.data.Forms._
    Form(
      mapping(
        "username" -> nonEmptyText,
        "password" -> nonEmptyText
      )(TokenFormInput.apply)(TokenFormInput.unapply)
    )
  }

  def tokenRequest = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { badForm: Form[TokenFormInput] =>
      // form validation/binding failed...
      BadRequest(badForm.errorsAsJson)
    }
    val successFunction = { input: TokenFormInput =>
      if (UserDao.validateUser(input.username, input.password)) {
        val token = getToken(input)
        Ok(s"token: ${token}")
      } else {
        Ok(s"Bad credentials")
      }
    }
    val formValidationResult: Form[TokenFormInput] = formToken.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  private def getToken(input: TokenFormInput): String = {
    val inputString = Json.obj(("user", input.username),("password", input.password)).toString()
    util.createToken(inputString)
  }

}
