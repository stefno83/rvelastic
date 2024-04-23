package rvelastic.controller;

import rvelastic.client.OpenSearchClientWrapper;
import rvelastic.configuration.AppConfiguration;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Listener {

  AppConfiguration appConfiguration;
  OpenSearchClientWrapper openSearchClientWrapper;

  @KafkaListener(topics = "${custom.kafka.source.topic}")
  public void readMessage(String message) throws IOException {

    log.info("Read message {}", message);

    JSONObject jsonObject = new JSONObject(message);

    log.info("Sending message {} on index {}", message, appConfiguration.getOpensearch().getIndex());

    openSearchClientWrapper.putDataOnIndex(jsonObject, appConfiguration.getOpensearch().getIndex());

    log.info("Message {} sent", message);

  }

}
