package com.spire.restbridge.model;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GroupInfo JSON serialization.
 */
class GroupInfoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serialize_flagsTrue_writesIsPrivateIsDynamicAndStringFields() {
        GroupInfo group = GroupInfo.builder()
                .isPrivate(true)
                .isDynamic(true)
                .ownerName("dmadmin")
                .groupAddress("group@example.com")
                .groupSource("LDAP")
                .build();

        JsonNode json = objectMapper.valueToTree(group);

        assertTrue(json.get("isPrivate").asBoolean());
        assertTrue(json.get("isDynamic").asBoolean());
        assertEquals("dmadmin", json.get("ownerName").asString());
        assertEquals("group@example.com", json.get("groupAddress").asString());
        assertEquals("LDAP", json.get("groupSource").asString());
        assertFalse(json.has("private"));
        assertFalse(json.has("dynamic"));
    }

    @Test
    void serialize_flagsFalse_writesIsPrivateAndIsDynamicFalse() {
        GroupInfo group = GroupInfo.builder()
                .isPrivate(false)
                .isDynamic(false)
                .build();

        JsonNode json = objectMapper.valueToTree(group);

        assertFalse(json.get("isPrivate").asBoolean());
        assertFalse(json.get("isDynamic").asBoolean());
    }
}
