package rvelastic.configuration;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom")
public class AppConfiguration {

  Kafka kafka;

  @Data
  public static class Kafka {
    Destination destination;
    String address;

  }
  @Data
  public static class Destination {
    String topic;
  }

}
