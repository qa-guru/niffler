package guru.qa.niffler.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

  public static final String ERROR_MESSAGE_PARAM = "errorMessage";
  public static final String ORIGINAL_CODE_PARAM = "originalCode";
  public static final String SESSION_ID_PARAM = "sessionId";
  public static final String COOKIES_PARAM = "cookies";

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.FOUND)
  public String handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    LOG.error("### Resolve HttpRequestMethodNotSupportedException in @RestControllerAdvice and redirect to /error page", ex);
    logRequestData(request, redirectAttributes, ex, HttpStatus.METHOD_NOT_ALLOWED);
    return "redirect:/error";
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.FOUND)
  public String handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    LOG.error("### Resolve NoResourceFoundException in @RestControllerAdvice and redirect to /error page", ex);
    logRequestData(request, redirectAttributes, ex, HttpStatus.NOT_FOUND);
    return "redirect:/error";
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.FOUND)
  public String handleException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    LOG.error("### Resolve Exception in @RestControllerAdvice and redirect to /error page", ex);
    logRequestData(request, redirectAttributes, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    return "redirect:/error";
  }

  private void logRequestData(@Nullable HttpServletRequest request,
                              @Nonnull RedirectAttributes redirectAttributes,
                              @Nonnull Exception ex,
                              @Nonnull HttpStatus status) {
    if (request != null) {
      final Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        List<String> cookieList = Arrays.stream(cookies)
            .map(cookie -> {
              LOG.info("### Cookie: {} = {}", cookie.getName(), cookie.getValue());
              return cookie.getName() + " = " + cookie.getValue();
            })
            .toList();
        redirectAttributes.addFlashAttribute(COOKIES_PARAM, cookieList);
      } else {
        redirectAttributes.addFlashAttribute(COOKIES_PARAM, List.of("No cookies found"));
      }
      final HttpSession httpSession = request.getSession(false);
      if (httpSession != null) {
        redirectAttributes.addFlashAttribute(SESSION_ID_PARAM, httpSession.getId());
      }
    }
    redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, ex.getMessage());
    redirectAttributes.addFlashAttribute(ORIGINAL_CODE_PARAM, status.value());
  }
}
