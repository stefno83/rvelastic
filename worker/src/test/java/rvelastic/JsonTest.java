package rvelastic;

import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class JsonTest {

  @Test
  @SneakyThrows
  void testAddField() {

    String newKey = "newKey";
    String newValue = "newValue";

    String testValue = "{'foo':'bar'}";

    JSONObject jsonObject = new JSONObject(testValue);
    jsonObject.put(newKey, newValue);

    Assertions.assertTrue(Objects.nonNull(jsonObject.getString(newKey)));
    Assertions.assertEquals(newValue, jsonObject.getString(newKey));
  }

}
