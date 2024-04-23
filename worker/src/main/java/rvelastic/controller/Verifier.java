package rvelastic.controller;

import rvelastic.configuration.AppConfiguration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Verifier {

  AppConfiguration appConfiguration;

  public String verify(String message) {
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> request = new HttpEntity<>(message);
    return restTemplate.postForObject(appConfiguration.getVerifier().getAddress(), request, String.class);
  }

}
