package rvelastic.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom")
public class AppConfiguration {

  Kafka kafka;
  Verifier verifier;

  @Data
  public static class Kafka {
    Source source;
    Destination destination;
    String address;
    String group;
  }
  @Data
  public static class Destination {
    String topic;
  }

  @Data
  public static class Source {
    String topic;
  }

  @Data
  public static class Verifier {
    String address;
    String field;
  }

}
