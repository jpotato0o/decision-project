package com.majorhelper.decision;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smile.Network;

public class InfluenceDiagram
{
    private Network net;
    private Map<String, Integer> decisionNodes = new HashMap<String, Integer>(); // node name and number
    private static final int VALUE_NODE_TYPE = 8;
    public static Map<String,String> majorMap = new HashMap<String,String>(); // major id and nice looking name 
    static {
        majorMap.put("agriculture_and_natural_resources", "Agriculture and Natural Resources");
        majorMap.put("arts", "Arts");
        majorMap.put("biology_and_life_science", "Biology and Life Science");
        majorMap.put("business", "Business");
        majorMap.put("communications_and_journalism", "Communications and Journalism");
        majorMap.put("computers_and_mathematics", "Computers and Mathematics");
        majorMap.put("education", "Education");
        majorMap.put("engineering", "Engineering");
        majorMap.put("health", "Health");
        majorMap.put("humanities_and_liberal_arts", "Humanities and Liberal Arts");
        majorMap.put("industrial_arts_and_consumer_services", "Industrial Arts and Consumer Services");
        majorMap.put("law_and_public_policy", "Law and Public Policy");
        majorMap.put("physical_sciences", "Physical Sciences");
        majorMap.put("psychology_and_social_work", "Psychology and Social Work");
        majorMap.put("social_science", "Social Science");
    }
    
    public InfluenceDiagram(Map<String,String[]> parameterMap) {
        net = new Network();
        String resourcesPath = System.getenv("MH_RESOURCES");
        String xdslFileIn = (new File(resourcesPath, "models/model.xdsl")).toString();
        net.readFile(xdslFileIn);
        
        // First get all the value nodes and their initial definitions
        Map<String, double[]> valueNodeDefinitions = new HashMap<String, double[]>();
        for (int i = 0; i < net.getNodeCount(); i++) {
            int curNodeType = net.getNodeType(i);
            String curNodeName = net.getNodeName(i);
            if (curNodeType == VALUE_NODE_TYPE) {
                double[] curNodeDefinition = net.getNodeDefinition(curNodeName);
                valueNodeDefinitions.put(curNodeName, curNodeDefinition);
            }
        }
        
        // Update the definitions hash map with the value from the user
        for (String key : parameterMap.keySet()) {
            String keyStart = key.substring(0,3);
            String keyEnd = key.substring(3);
            if (keyStart.equals("_v_")) {
                String[] split = keyEnd.split("-");
                String node = split[0];
                int possibilityIndex = Integer.parseInt(split[1]);
                double possibilityValue = Double.parseDouble(parameterMap.get(key)[0]);
                valueNodeDefinitions.get(node)[possibilityIndex] = possibilityValue;
            } else if  (keyStart.equals("_r_")) {
                int possibilityIndex = Integer.parseInt(parameterMap.get(key)[0]);
                double possibilityValue = 100;
                valueNodeDefinitions.get(keyEnd)[possibilityIndex] = possibilityValue;
            }
        }
        
        // Scale values
        scaleValues(valueNodeDefinitions);
        
        // Lastly, put these updated values in the network
        for (String key : valueNodeDefinitions.keySet()) {
            net.setNodeDefinition(key, valueNodeDefinitions.get(key));
        }
        
        // Get all the value node weights and the corresponding input
        Map<String, Double> valueNodeWeights = new HashMap<String, Double>();
        for (String key : parameterMap.keySet()) {
            String keyStart = key.substring(0,3);
            String keyEnd = key.substring(3);
            if (keyStart.equals("_a_")) {
                double curWeight = Double.parseDouble(parameterMap.get(key)[0]);
                valueNodeWeights.put(keyEnd,curWeight);
            }
        }
        // Set total_value with right weights
        String[] networkValueNodes = net.getParentIds("total_value");
        double[] total_value_definition = new double[networkValueNodes.length];
        for (int i = 0; i < networkValueNodes.length; i++) {
            String curNetworkValueNode = networkValueNodes[i];
            // Salary is always the base and has weight equal to 1
            if (curNetworkValueNode.equals("salary_value")) {
                total_value_definition[i] = 1;
            } else if (valueNodeWeights.get(curNetworkValueNode) != null) {
                total_value_definition[i] = valueNodeWeights.get(curNetworkValueNode);
            } else {
                total_value_definition[i] = 0;
            }
        }
        net.setNodeDefinition("total_value", total_value_definition);
        
        net.updateBeliefs();
        
        // Output a model (for testing purposes to see if model is updated correctly
        @SuppressWarnings("unused")
        String xdslFileOut = (new File(resourcesPath, "models/modelOut.xdsl")).toString();
        //net.writeFile(xdslFileOut);
        
        // This is useless since you only have one decision in the model
        // You were originally anticipating multiple decisions
        int[] aValueIndexingParents = net.getValueIndexingParents("total_value");
        for (int i : aValueIndexingParents) {
            int nodeDecision = i;
            String decisionName = net.getNodeName(nodeDecision);
            decisionNodes.put(decisionName, nodeDecision);
        }
    }
    
    // Scale Value Nodes so that min value is 0 and max value 100
    public static void scaleValues(Map<String, double[]> valueNodeDefinitions) {
        for (String key : valueNodeDefinitions.keySet()) {
            double[] curDef = valueNodeDefinitions.get(key);
            double curMin = curDef[0];
            double curMax = curDef[0];
            for (int i = 1; i < curDef.length; i++) {
                if (curDef[i] < curMin) {
                    curMin = curDef[i];
                }
                if (curDef[i] > curMax) {
                    curMax = curDef[i];
                }
            }
            double diff = curMax - curMin;
            for (int i = 0; i < curDef.length; i++) {
                if (diff != 0) {
                    curDef[i] = (curDef[i] - curMin) / diff;
                    curDef[i] = curDef[i] * 100;                    
                }
                else {
                    // This is not necessary since if they all have the same value, there's indifference
                    curDef[i] = 0;
                }
            }
        }
    }
    
    public List<Decision> expectedUtilities(String decision, String utilityNode) {
        int nodeDecision = decisionNodes.get(decision);
        List<Decision> decisions = new ArrayList<Decision>();
        int outcomes = net.getOutcomeCount(nodeDecision);
        for (int i = 0; i < outcomes; i++) {
            String parentOutcomeId = net.getOutcomeId(nodeDecision, i);
            double expectedUtility = net.getNodeValue(utilityNode)[i];
            decisions.add(new Decision(parentOutcomeId, expectedUtility));
        }
        Collections.sort(decisions);
        normalizeDecisions(decisions);
        return decisions;
    }
    
    public static class Decision implements Comparable<Decision> {
        public String decision;
        public double utility;
        public Decision(String decision, double utility) {
            this.decision = decision;
            this.utility = utility;
        }
        // Sort Descending
        public int compareTo(Decision d) {
            if (this.utility > d.utility) {
                return -1;
            } else if (this.utility == d.utility) {
                return 0;
            } else {
                return 1;
            }
        }
    }
    
    // Normalize a list of decisions so that the utilities range from 0 to 100.
    // Assumes sorted already
    // If they're all the same, then set them all to 100
    private static void normalizeDecisions(List<Decision> sortedDecisions) {
        double max = sortedDecisions.get(0).utility;
        double min = sortedDecisions.get(sortedDecisions.size()-1).utility;
        double diff = max - min;
        for (Decision cur : sortedDecisions) {
            if (diff != 0) {
                cur.utility = (cur.utility - min) / diff;
                cur.utility = cur.utility * 100;                
            }
            else {
                cur.utility = 100;
            }
        }
    }
    
    // There was no jsmile functionality for this so you had to write your own.
    @SuppressWarnings("unused")
    private static void setDeterministicNodeValue(Network net, String nodeId, String outcomeId) {
        int outcomeCount = net.getOutcomeCount(nodeId);
        double[] updatedDefinition = new double[outcomeCount];
        for (int i = 0; i < outcomeCount; i++) {
            if (net.getOutcomeId(nodeId,i).equals(outcomeId))
                updatedDefinition[i] = 1.0;
            else
                updatedDefinition[i] = 0.0;
        }
        net.setNodeDefinition(nodeId, updatedDefinition);
    }
}
