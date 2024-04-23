package rvelastic.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom")
public class AppConfiguration {

  Kafka kafka;
  Opensearch opensearch;

  @Data
  public static class Kafka {
    Source source;
    String address;
    String group;
  }
  @Data
  public static class Source {
    String topic;
  }

  @Data
  public static class Opensearch {
    String address;
    String index;
    String user;
    String password;
    int port;
  }

}
