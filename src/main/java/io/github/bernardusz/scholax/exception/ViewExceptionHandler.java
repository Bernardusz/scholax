package io.github.bernardusz.scholax.exception;

import io.github.bernardusz.scholax.exception.dto.ErrorResponse;
import io.github.bernardusz.scholax.exception.exceptions.UserNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ViewExceptionHandler {
  @ExceptionHandler(UserNotFound.class)
  public String handleUserNotFind(UserNotFound ex, Model model) {
    model.addAttribute("error", new ErrorResponse(
      ex.getErrorCode(),
      ex.getMessage(),
      LocalDateTime.now()
    ));
    return "error";
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public String handlePageNotFound(HttpServletRequest request){
    String uri = request.getRequestURI();

    if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/")) {
      return null;
    }

    return "redirect:/";
  }
}
