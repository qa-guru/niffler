package guru.qa.niffler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static guru.qa.niffler.service.GlobalExceptionHandler.ERROR_MESSAGE_PARAM;
import static guru.qa.niffler.service.GlobalExceptionHandler.ORIGINAL_CODE_PARAM;
import static guru.qa.niffler.service.GlobalExceptionHandler.SESSION_ID_PARAM;

@Controller
public class ErrorAuthController implements ErrorController {

  private static final String ERROR_VIEW_NAME = "error";

  private final String nifflerFrontUri;

  public ErrorAuthController(@Value("${niffler-front.base-uri}") String nifflerFrontUri) {
    this.nifflerFrontUri = nifflerFrontUri;
  }

  @GetMapping("/error")
  @ResponseStatus(HttpStatus.OK)
  public String error(HttpServletRequest request,
                      HttpServletResponse response,
                      Model model) {
    final Object originalCode = model.getAttribute(ORIGINAL_CODE_PARAM);
    final Object originalError = model.getAttribute(ERROR_MESSAGE_PARAM);

    final String message;
    final int status = originalCode != null  ? Integer.parseInt(originalCode.toString()) : response.getStatus();

    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    if (originalError != null) {
      message = originalError.toString();
    } else if (throwable != null) {
      message = throwable.getMessage();
    } else {
      message = "Unknown error";
    }

    model.addAttribute("status", status);
    model.addAttribute("frontUri", nifflerFrontUri + "/main");
    model.addAttribute("error", message);
    model.addAttribute("sessionId", model.getAttribute(SESSION_ID_PARAM));
    return ERROR_VIEW_NAME;
  }
}
