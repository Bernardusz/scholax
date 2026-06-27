package io.github.bernardusz.scholax.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request){
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null && Integer.parseInt(status.toString()) == 404){
      Object originalUrlObj = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
      if (originalUrlObj != null){
        String uri = originalUrlObj.toString();

        if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/")) {
          return "error"; // Falls back to default error page rendering for assets
        }
        return "redirect:/";
      }
    }
    return "error";
  }
}
