package guru.qa.niffler;

import guru.qa.niffler.service.PropertiesLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NifflerGatewayApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(NifflerGatewayApplication.class);
    springApplication.addListeners(new PropertiesLogger());
    springApplication.run(args);
  }

}
