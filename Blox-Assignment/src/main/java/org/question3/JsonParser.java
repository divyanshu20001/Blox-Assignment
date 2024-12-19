package org.question3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * A utility class for parsing JSON strings into Java objects.
 * <p>
 * Supports converting JSON to:
 * <ul>
 *     <li>{@code Map<String, Object>} for JSON objects</li>
 *     <li>{@code List<Object>} for JSON arrays</li>
 *     <li>Primitive types like {@code String}, {@code BigDecimal}, and {@code Boolean}</li>
 * </ul>
 * Ensures high precision for numeric values.
 */
public class JsonParser {

    /**
     * Parses a JSON string into a Java object.
     *
     * @param jsonString The JSON string to parse (must not be null or empty).
     * @return A Java object matching the JSON structure.
     * @throws IllegalArgumentException If the JSON string is null or empty.
     * @throws Exception If any parsing error occurs.
     */
    public static Object parseJson(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        JsonNode rootNode = objectMapper.readTree(jsonString);
        if (rootNode.isObject()) {
            return objectMapper.convertValue(rootNode, new TypeReference<Map<String, Object>>() {});
        } else if (rootNode.isArray()) {
            return objectMapper.convertValue(rootNode, new TypeReference<List<Object>>() {});
        } else if (rootNode.isValueNode()) {
            if (rootNode.isBigDecimal() || rootNode.isDouble() || rootNode.isFloat()) {
                return new BigDecimal(rootNode.asText());
            } else if (rootNode.isInt() || rootNode.isLong() || rootNode.isBigInteger()) {
                return rootNode.bigIntegerValue();
            } else if (rootNode.isBoolean()) {
                return rootNode.booleanValue();
            } else {
                return rootNode.asText();
            }
        } else {
            throw new IllegalArgumentException("Invalid JSON structure");
        }
    }
    public static void main(String[] args) {
        String jsonString = """
                {
                  "name": "Divyanshu",
                  "age": 24,
                  "salary": 12345678901234567890.12345,
                  "isEmployed": true,
                  "hobbies": ["Reading", "Gaming", "Coding"],
                  "address": {
                    "city": "Agra",
                    "zip": 12345
                  }
                }
                """;
        try {
            Object result = parseJson(jsonString);
            System.out.println("Parsed Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}