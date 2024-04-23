package rvelastic.controller;


import rvelastic.configuration.AppConfiguration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.json.JSONObject;

@Slf4j
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Listener
{

  Verifier verifier;

  KafkaTemplate<String,String> kafkaTemplate;

  AppConfiguration appConfiguration;

  @KafkaListener(topics = "${custom.kafka.source.topic}")
  public void readMessage(String message) {

    log.info("Read message {}", message);

    log.info("Verify message {}", message);
    String result = verifier.verify(message);
    log.info("Verification of property {} is {}", appConfiguration.getVerifier().getField(), result);

    JSONObject verdictObject = new JSONObject(result);
    String verdictString = verdictObject.getString("verdict");

    JSONObject jsonObject = new JSONObject(message);
    jsonObject.put(appConfiguration.getVerifier().getField(), "always_true".equals(verdictString));

    String topicName = appConfiguration.getKafka().getDestination().getTopic();

    log.info("Send message enriched {} on topic {}", jsonObject, topicName);

    kafkaTemplate.send(topicName, jsonObject.toString());


  }

}
