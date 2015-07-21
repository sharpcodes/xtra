package org.rest.automation.jsonparser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.exceptions.JsonPathException;

import java.util.Iterator;

/**
 * JsonParser that extracts a node value given a path in the following format
 * <root-node>.<node1>.<node2>.. starting with the parent
 */
public class SimpleJsonParser {

    private static Logger LOGGER = LogManager.getLogger(SimpleJsonParser.class);

    public String parseExpression(String expression, String json) throws JsonPathException {

        String[] tokens = expression.split("\\.");
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objMapper.readTree(json);
        } catch (Exception e) {
            LOGGER.error("error when parsing json" + e.getMessage());
            throw new JsonPathException("Invalid Json found -->" + json);
        }
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].endsWith("]")) {
                String nodeName = tokens[i].substring(0, tokens[i].length() - 3);
                jsonNode = jsonNode.path(nodeName);
                int chosenIndex=0;
                String extractedIndex=tokens[i].substring(tokens[i].indexOf("[") + 1, tokens[i].length() - 1);
                try {
                     chosenIndex = Integer.parseInt(tokens[i].substring(tokens[i].indexOf("[") + 1, tokens[i].length() - 1));
                }catch (NumberFormatException e){
                    throw new JsonPathException("Please provide valid integer values when accessing array -->"+expression);
                }
                LOGGER.debug("chosen index =" + chosenIndex);
                if (chosenIndex >= 0) {
                    Iterator<JsonNode> elements = jsonNode.elements();
                    int k = 0;
                    while (elements.hasNext()) {
                        if (k == chosenIndex) {
                            jsonNode = elements.next();
                            if (jsonNode.isValueNode()) {
                                return jsonNode.asText().length() > 0 ? jsonNode.asText() : null;
                            }
                            break;
                        }
                        elements.next();
                        k++;
                    }
                }

            } else {
                if (jsonNode.isValueNode() && i < tokens.length - 1) {
                    throw new JsonPathException("expression-->" + expression + " is invalid");
                }
                jsonNode = jsonNode.path(tokens[i]);
            }
        }
        if (!jsonNode.isValueNode()) {
            throw new JsonPathException("Cannot resolve json node--> " + expression + " in json object -->" + json);
        } else {
            return jsonNode.asText().length() > 0 ? jsonNode.asText() : null;
        }
    }



}
