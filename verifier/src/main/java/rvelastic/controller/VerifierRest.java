package rvelastic.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class VerifierRest {

  final Map<String, String> lockMap = new HashMap<>();
  final Map<String, String> roleMap = Map.of(
      "mrapotti", "VIEW",
      "cgandolfo", "VIEW",
      "emartini", "EDITOR",
      "acarini", "EDITOR",
      "sparini", "ADMIN");

  @PostMapping(value = "/verify")
  public String verify(@RequestBody String message) {

    log.info("Verify message {}", message);

    JSONObject jsonObject = new JSONObject(message);

    String action = jsonObject.getJSONObject("action").getString("name");
    String username = jsonObject.getString("username");
    String object = jsonObject.getJSONObject("object").getString("name");

    log.info("Verify action {}, username {}, object {}", action, username, object);
    String role = roleMap.getOrDefault(username, "VIEW");
    log.info("Role for user {} is {}", username, role);

    return verified(action, object, role, username );

  }

  private String verified(String action, String object, String role, String username) {

    boolean verdict = true;

    switch (action) {
      case "VIEW":
      case "PRINT":
        break;
      case "LOCK":
        verdict = checkLock(object, role, username);
        break;
      case "UNLOCK":
        verdict = checkUnlock(object, role, username);
        break;
      case "EDIT":
        verdict = checkEdit(object, role, username);
        break;
      case "DELETE":
        verdict = isAdmin(role);
        break;
    }

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("verdict", verdict ?"always_true":"always_false");

    return jsonObject.toString();
  }

  private boolean checkEdit(String object, String role, String username) {
    if (canOperateOnObject(object, role, username)) {
      return false;
    }
    log.info("Edit dell'oggetto {} da parte di {}", object, username);
    return true;
  }

  private boolean canOperateOnObject(String object, String role, String username) {
    if (isViewer(role)) {
      return true;
    }
    if (!lockMap.containsKey(object)) {
      log.info("L'oggetto {} non è lockato", object);
      return true;
    }
    if (!lockMap.get(object).equals(username) && !isAdmin(role)) {
      log.info("L'oggetto {} non è lockato dall'utente {}", object, username);
      return true;
    }
    return false;
  }

  private boolean checkUnlock(String object, String role, String username) {
    if (canOperateOnObject(object, role, username)) {
      return false;
    }
    lockMap.remove(object);
    log.info("Unlock dell'oggetto {} da parte di {}", object, username);
    return true;
  }

  private boolean checkLock(String object, String role, String username) {
    if (isViewer(role)) {
      return false;
    }
    if (lockMap.containsKey(object)) {
      log.info("L'oggetto {} è già lockato", object);
      return false;
    }

    lockMap.put(object, username);
    log.info("Lock dell'oggetto {} da parte di {}", object, username);
    return true;
  }

  private static boolean isViewer(String role) {
    return role.equals("VIEW");
  }

  private static boolean isAdmin(String role) {
    return role.equals("ADMIN");
  }

  private static boolean isEditor(String role) {
    return role.equals("EDITOR");
  }

}
