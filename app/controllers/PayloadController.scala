package controllers

import javax.inject._
import play.api.data.Form
import play.api.mvc._
import util.JwtUtil

case class PayloadFormInput(token: String)

@Singleton
class PayloadController @Inject()(
                                  cc: MessagesControllerComponents,
                                  util: JwtUtil
                                ) extends MessagesAbstractController(cc) {
  private val formPayload: Form[PayloadFormInput] = {
    import play.api.data.Forms._
    Form(
      mapping(
        "token" -> nonEmptyText
      )(PayloadFormInput.apply)(PayloadFormInput.unapply)
    )
  }

  def payloadRequest = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { badForm: Form[PayloadFormInput] =>
      // form validation/binding failed...
      BadRequest(badForm.errorsAsJson)
    }
    val successFunction = { input: PayloadFormInput =>
      val payload = getPayload(input)
      Ok(s"payload: ${payload}")
    }
    val formValidationResult: Form[PayloadFormInput] = formPayload.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  private def getPayload(input: PayloadFormInput): String = {
    val token = s"${input.token}"
    if (!util.isValidToken(token)) {
      "invalid token"
    } else {
      util.decodePayload(token).getOrElse("empty")
    }
  }
}
