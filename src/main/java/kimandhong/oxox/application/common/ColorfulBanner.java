package kimandhong.oxox.application.common;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ColorfulBanner implements CommandLineRunner {
  private final Environment environment;

  public ColorfulBanner(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void run(String... args) {
    System.out.println("환경 변수 체크사항");
    System.out.printf("%-15s: %s%n", "NODE_ENV", environment.getProperty("NODE_ENV", "LOCAL"));
    System.out.printf("%-15s: %s%n", "DB_HOST", environment.getProperty("DB_HOST", "localhost"));
    System.out.printf("%-15s: %s%n", "SERVER_URL", environment.getProperty("SERVER_URL", "http://localhost:8080"));
    System.out.printf("%-15s: %s%n", "SWAGGER_URL", environment.getProperty("SWAGGER_URL", "http://localhost:8080/docs"));
  }
}
