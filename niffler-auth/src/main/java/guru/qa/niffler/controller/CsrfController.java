package guru.qa.niffler.controller;

import guru.qa.niffler.model.CsrfJson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

  private final CsrfTokenRepository csrfTokenRepository;

  @Autowired
  public CsrfController(CsrfTokenRepository csrfTokenRepository) {
    this.csrfTokenRepository = csrfTokenRepository;
  }

  @GetMapping("/csrf/generate")
  public CsrfJson generate(HttpServletRequest request,
                           HttpServletResponse response) {

    CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
    csrfTokenRepository.saveToken(csrfToken, request, response);
    return new CsrfJson(
        csrfToken.getToken()
    );
  }
}
