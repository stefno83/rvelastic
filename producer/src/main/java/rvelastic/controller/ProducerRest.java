package rvelastic.controller;

import rvelastic.configuration.AppConfiguration;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@AllArgsConstructor
public class ProducerRest {

  KafkaTemplate<String,String> kafkaTemplate;
  AppConfiguration appConfiguration;

  @PostMapping(value = "/send-message", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void sendMessage(@RequestBody String message) {

    log.info("Send message {} on topic {}", message, appConfiguration.getKafka().getDestination().getTopic());

    kafkaTemplate.send(appConfiguration.getKafka().getDestination().getTopic(), message);

  }
  @PostMapping(value = "/create-sample-data", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createSampleData(@RequestBody int number) {

    Random rand = new Random();

    List<String> authors = List.of("Author1","Author2","Author3", "Author4", "Author5");
    List<String> titles = List.of("Title1","Title2","Title3", "Title4", "Title5");
    List<String> costs = List.of("10","20","30","40","50");

    for (int i=0; i<number; i++) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("author", authors.get(rand.nextInt(authors.size())));
      jsonObject.put("title", titles.get(rand.nextInt(titles.size())));
      jsonObject.put("cost", costs.get(rand.nextInt(costs.size())));
      jsonObject.put("date", new Date());

      log.info("Send message {} on topic {}", jsonObject, appConfiguration.getKafka().getDestination().getTopic());

      kafkaTemplate.send(appConfiguration.getKafka().getDestination().getTopic(), jsonObject.toString());
    }

  }

  @PostMapping(value = "/create-user-action", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createUserAction(@RequestBody String message) {

    JSONObject jsonObject = new JSONObject(message);
    jsonObject.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    jsonObject.put("type", "user.action.v1");

    log.info(jsonObject.toString());

    kafkaTemplate.send(appConfiguration.getKafka().getDestination().getTopic(), jsonObject.toString());

  }

  @PostMapping(value = "/create-data-action", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createDateAction(@RequestBody String message) {

    JSONObject jsonObject = new JSONObject(message);
    jsonObject.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    jsonObject.put("type", "data.action.v1");

    log.info(jsonObject.toString());
    kafkaTemplate.send(appConfiguration.getKafka().getDestination().getTopic(), jsonObject.toString());

  }

  @PostMapping(value = "/create-logging-action", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createLogginAction(@RequestBody String message) {

    JSONObject jsonObject = new JSONObject(message);
    jsonObject.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    log.info("Create loging action {}", jsonObject);
    kafkaTemplate.send(appConfiguration.getKafka().getDestination().getTopic(), jsonObject.toString());

  }

}
