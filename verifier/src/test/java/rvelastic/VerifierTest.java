package rvelastic;

import rvelastic.controller.VerifierRest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author smurino smurino@doxee.com
 * @createDate 02/01/24
 * <p>
 * Copyright (C) 2024 Doxee S.p.A. C.F. - P.IVA: IT02714390362. All Rights Reserved
 */

@Slf4j
class VerifierTest {

  @Test
  void testViewer() {

    VerifierRest verifierRest = new VerifierRest();

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("action", new JSONObject().put("name", "VIEW"));
    jsonObject.put("username", "mrapotti");
    jsonObject.put("object",  new JSONObject().put("name", "object.123"));

    // ------------------------ VIEW ------------------------
    //VIEW role VIEW
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ PRINT ------------------------
    //PRINT role VIEW
    jsonObject.getJSONObject("action").put("name", "PRINT");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ LOCK ------------------------
    //LOCK role VIEW
    jsonObject.getJSONObject("action").put("name", "LOCK");
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ UNLOCK ------------------------
    //UNLOCK role VIEW
    jsonObject.getJSONObject("action").put("name", "UNLOCK");
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ EDIT ------------------------
    //EDIT role VIEW
    jsonObject.getJSONObject("action").put("name", "EDIT");
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ DELETE ------------------------
    //DELETE role VIEW
    jsonObject.getJSONObject("action").put("name", "DELETE");
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------
  }

  @Test
  void testEditor() {

    VerifierRest verifierRest = new VerifierRest();

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("action", new JSONObject().put("name", "VIEW"));
    jsonObject.put("username", "emartini");
    jsonObject.put("object", new JSONObject().put("name", "object.123"));

    // ------------------------ VIEW ------------------------
    //VIEW role EDITOR
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ PRINT ------------------------
    //PRINT role EDITOR
    jsonObject.getJSONObject("action").put("name", "PRINT");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ LOCK ------------------------
    //LOCK role EDITOR object not locked
    jsonObject.getJSONObject("action").put("name", "LOCK");
    jsonObject.put("object", new JSONObject().put("name", "lock.123"));
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));

    //LOCK role EDITOR object already locked
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ UNLOCK ------------------------
    //UNLOCK role EDITOR object not locked
    jsonObject.getJSONObject("action").put("name", "UNLOCK");
    jsonObject.put("object", new JSONObject().put("name", "unlock.123"));
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));

    //UNLOCK role EDITOR object locked
    jsonObject.getJSONObject("action").put("name", "LOCK");
    verifierRest.verify(jsonObject.toString());
    jsonObject.getJSONObject("action").put("name", "UNLOCK");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));

    //UNLOCK role EDITOR object locked from another user
    jsonObject.getJSONObject("action").put("name", "LOCK");
    jsonObject.put("username", "acarini");
    verifierRest.verify(jsonObject.toString());
    jsonObject.put("username", "emartini");
    jsonObject.getJSONObject("action").put("name", "UNLOCK");
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ EDIT ------------------------
    //EDIT role EDITOR object not locked
    jsonObject.getJSONObject("action").put("name", "EDIT");
    jsonObject.put("object", new JSONObject().put("name", "edit.1"));
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));

    //EDIT role EDITOR object locked
    jsonObject.getJSONObject("action").put("name", "LOCK");
    verifierRest.verify(jsonObject.toString());
    jsonObject.getJSONObject("action").put("name", "EDIT");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));

    //EDIT role EDITOR object locked from another user
    jsonObject.put("object", new JSONObject().put("name", "edit.2"));
    jsonObject.getJSONObject("action").put("name", "LOCK");
    jsonObject.put("username", "acarini");
    verifierRest.verify(jsonObject.toString());
    jsonObject.put("username", "emartini");
    jsonObject.getJSONObject("action").put("name", "EDIT");
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ DELETE ------------------------
    //DELETE role EDITOR
    jsonObject.getJSONObject("action").put("name", "DELETE");
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

  }

  @Test
  void testAdmin() {

    VerifierRest verifierRest = new VerifierRest();

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("action", new JSONObject().put("name", "VIEW"));
    jsonObject.put("username", "sparini");
    jsonObject.put("object", new JSONObject().put("name", "object.123"));

    // ------------------------ VIEW ------------------------
    //VIEW role ADMIN
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ PRINT ------------------------
    //PRINT role ADMIN
    jsonObject.getJSONObject("action").put("name", "PRINT");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ LOCK ------------------------
    //LOCK role ADMIN object not locked
    jsonObject.getJSONObject("action").put("name", "LOCK");
    jsonObject.put("object", new JSONObject().put("name", "lock.123"));
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));

    //LOCK role ADMIN object already locked
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ UNLOCK ------------------------
    //UNLOCK role ADMIN object not locked
    jsonObject.getJSONObject("action").put("name", "UNLOCK");
    jsonObject.put("object", new JSONObject().put("name", "unlock.123"));
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));

    //UNLOCK role ADMIN object locked
    jsonObject.getJSONObject("action").put("name", "LOCK");
    verifierRest.verify(jsonObject.toString());
    jsonObject.getJSONObject("action").put("name", "UNLOCK");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));

    //UNLOCK role ADMIN object locked from another user
    jsonObject.getJSONObject("action").put("name", "LOCK");
    jsonObject.put("username", "acarini");
    verifierRest.verify(jsonObject.toString());
    jsonObject.put("username", "sparini");
    jsonObject.getJSONObject("action").put("name", "UNLOCK");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

    // ------------------------ EDIT ------------------------

    //EDIT role ADMIN object not locked
    jsonObject.getJSONObject("action").put("name", "EDIT");
    jsonObject.put("object", new JSONObject().put("name", "edit.1"));
    Assertions.assertFalse(verifierRest.verify(jsonObject.toString()));

    //EDIT role ADMIN object locked
    jsonObject.getJSONObject("action").put("name", "LOCK");
    verifierRest.verify(jsonObject.toString());
    jsonObject.getJSONObject("action").put("name", "EDIT");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));

    //EDIT role ADMIN object locked from another user
    jsonObject.put("object", new JSONObject().put("name", "edit.2"));
    jsonObject.getJSONObject("action").put("name", "LOCK");
    jsonObject.put("username", "acarini");
    verifierRest.verify(jsonObject.toString());
    jsonObject.put("username", "sparini");
    jsonObject.getJSONObject("action").put("name", "EDIT");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));

    // ------------------------------------------------------

    // ------------------------ DELETE ------------------------
    //DELETE role ADMIN
    jsonObject.getJSONObject("action").put("name", "DELETE");
    Assertions.assertTrue(verifierRest.verify(jsonObject.toString()));
    // ------------------------------------------------------

  }


}
