package com.moviedecision;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smile.Network;
import smile.SMILEException;

public class MInfluenceDiagram {
	public void CreateNetwork() {
		 try {
		   Network net = new Network();
		   
		   // Creating node "Success" and setting/adding outcomes:
		   net.addNode(Network.NodeType.Cpt, "Actor_Value");
		   net.setOutcomeId("Actor_Value", 0, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Director_s_Value");
		   net.setOutcomeId("Director_s_Value", 0, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "HSX");
		   net.setOutcomeId("HSX", 1, "Actor_Value");
		   net.setOutcomeId("HSX", 1, "Director_s_Value");
		   
		   net.addNode(Network.NodeType.Cpt, "IMDB");
		   net.setOutcomeId("IMDB", 0, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Rotten_Tomato");
		   net.setOutcomeId("Director_s_Value", 0, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Metacritic");
		   net.setOutcomeId("Metacritic", 0, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Review");
		   net.setOutcomeId("Review", 1, "IMDB");
		   net.setOutcomeId("Review", 1, "Rotten_Tomato");
		   net.setOutcomeId("Review", 1, "Metacritic");
		   
		   net.addNode(Network.NodeType.Cpt, "Quality");
		   net.setOutcomeId("Quality", 1, "value");
		   
		   
		   
		   
		   // Creating node "Forecast" and setting/adding outcomes:
		   net.addNode(Network.NodeType.Cpt, "Forecast");  
		   net.addOutcome("Forecast", "Good");
		   net.addOutcome("Forecast", "Moderate");
		   net.addOutcome("Forecast", "Poor");
		   net.deleteOutcome("Forecast", 0);
		   net.deleteOutcome("Forecast", 0);
		   
		   // Adding an arc from "Success" to "Forecast":
		   net.addArc("Success", "Forecast");
		   
		   // Filling in the conditional distribution for node "Success". The 
		   // probabilities are:
		   // P("Success" = Success) = 0.2
		   // P("Success" = Failure) = 0.8
		   double[] aSuccessDef = {0.2, 0.8};
		   net.setNodeDefinition("Success", aSuccessDef);
		   
		   // Filling in the conditional distribution for node "Forecast". The 
		   // probabilities are:
		   // P("Forecast" = Good | "Success" = Success) = 0.4
		   // P("Forecast" = Moderate | "Success" = Success) = 0.4
		   // P("Forecast" = Poor | "Success" = Success) = 0.2
		   // P("Forecast" = Good | "Success" = Failure) = 0.1
		   // P("Forecast" = Moderate | "Success" = Failure) = 0.3
		   // P("Forecast" = Poor | "Success" = Failure) = 0.6
		   double[] aForecastDef = {0.4, 0.4, 0.2, 0.1, 0.3, 0.6}; 
		   net.setNodeDefinition("Forecast", aForecastDef);
		   
		   // Changing the nodes' spacial and visual attributes:
		   net.setNodePosition("Actor_Value", 20, 20, 80, 30);
		   net.setNodeBgColor("Actor_Value", Color.red);
		   net.setNodeTextColor("Actor_Value", Color.white);
		   net.setNodeBorderColor("Actor_Value", Color.black);
		   net.setNodeBorderWidth("Actor_Value", 2);
		   net.setNodePosition("Forecast", 30, 100, 60, 30);
		   
		   // Writting the network to a file:
		   net.writeFile("models/MovieModel.xdsl");
		 }
		 catch (SMILEException e) {
		   System.out.println(e.getMessage());
		 }
	}
	
	public void InfereceWithBayesianNetwork() {
		 try {
		   Network net = new Network();
		   net.readFile("models/MovieModel.xdsl"); 
		   // ---- We want to compute P("Forecast" = Moderate) ----
		   // Updating the network:
		   net.updateBeliefs();
		   
		   // Getting the handle of the node "Forecast":
		   net.getNode("Forecast");
		   
		   // Getting the index of the "Moderate" outcome:
		   String[] aForecastOutcomeIds = net.getOutcomeIds("Forecast");
		   int outcomeIndex;
		   for (outcomeIndex = 0; outcomeIndex < aForecastOutcomeIds.length; outcomeIndex++)
		     if ("Moderate".equals(aForecastOutcomeIds[outcomeIndex]))
		       break;
		   
		   // Getting the value of the probability:
		   double[] aValues = net.getNodeValue("Forecast");
		   double P_ForecastIsModerate = aValues[outcomeIndex];
		   
		   System.out.println("P(\"Forecast\" = Moderate) = "      + P_ForecastIsModerate);
		   
		   
		   // ---- We want to compute P("Success" = Failure | "Forecast" = Good) ----
		   // Introducing the evidence in node "Forecast":
		   net.setEvidence("Forecast", "Good");
		   
		   // Updating the network:
		   net.updateBeliefs();
		   
		   // Getting the handle of the node "Success":
		   net.getNode("Actor_Value");
		   
		   // Getting the index of the "Failure" outcome:
		   String[] aSuccessOutcomeIds = net.getOutcomeIds("Actor_Value");
		   for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
		     if ("value".equals(aSuccessOutcomeIds[outcomeIndex]))
		       break;
		   
		   // Getting the value of the probability:
		   aValues = net.getNodeValue("Success");
		   double P_SuccIsFailGivenForeIsGood = aValues[outcomeIndex];
		   
		   System.out.println("P(\"Success\" = Failure | \"Forecast\" = Good) = " + P_SuccIsFailGivenForeIsGood);
		   
		   // ---- We want to compute P("Success" = Success | "Forecast" = Poor) ----
		   // Clearing the evidence in node "Forecast":
		   net.clearEvidence("Forecast");
		   
		   // Introducing the evidence in node "Forecast":
		   net.setEvidence("Forecast", "Good");
		   
		   // Updating the network:
		   net.updateBeliefs();
		   
		   // Getting the index of the "Failure" outcome:
		   aSuccessOutcomeIds = net.getOutcomeIds("Success");
		   for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
		     if ("Failure".equals(aSuccessOutcomeIds[outcomeIndex]))
		       break;
		   
		   // Getting the value of the probability:
		   aValues = net.getNodeValue("Success");
		   double P_SuccIsSuccGivenForeIsPoor = aValues[outcomeIndex];
		   
		   System.out.println("P(\"Success\" = Success | \"Forecast\" = Poor) = " + P_SuccIsSuccGivenForeIsPoor);
		 }
		 catch (SMILEException e) {
		   System.out.println(e.getMessage());
		 }
		}
	public void UpgradeToInfluenceDiagram() {
		 try {
		   Network net = new Network();
		   net.readFile("models/MovieModel.xdsl");
		   
		   // Creating node "Invest" and setting/adding outcomes:
		   net.addNode(Network.NodeType.List, "Invest");
		   net.setOutcomeId("Invest", 0, "Invest");
		   net.setOutcomeId("Invest", 1, "DoNotInvest");
		   
		   // Creating the value node "Gain":
		   net.addNode(Network.NodeType.Table, "Gain");
		   
		   // Adding an arc from "Invest" to "Gain":
		   net.addArc("Invest", "Gain");
		   
		   // Adding an arc from "Success" to "Gain":
		   net.getNode("Success");
		   net.addArc("Success", "Gain");
		   
		   // Filling in the utilities for the node "Gain". The utilities are:
		   // U("Invest" = Invest, "Success" = Success) = 10000
		   // U("Invest" = Invest, "Success" = Failure) = -5000
		   // U("Invest" = DoNotInvest, "Success" = Success) = 500
		   // U("Invest" = DoNotInvest, "Success" = Failure) = 500
		   double[] aGainDef = {10000, -5000, 500, 500};
		   net.setNodeDefinition("Gain", aGainDef);
		   
		   net.writeFile("models/MovieModel.xdsl");
		 }
		 catch (SMILEException e) {
		   System.out.println(e.getMessage());
		 }
		}
	public void InferenceWithInfluenceDiagram() {
		 try {
		   // Loading and updating the influence diagram: 
		   Network net = new Network();
		   net.readFile("models/MovieModel.xdsl");
		   net.updateBeliefs();
		   
		   // Getting the utility node's handle:
		   net.getNode("Gain");
		   
		   // Getting the handle and the name of value indexing parent (decision node):
		   int[] aValueIndexingParents = net.getValueIndexingParents("Gain");
		   int nodeDecision = aValueIndexingParents[0];
		   String decisionName = net.getNodeName(nodeDecision);
		   
		   // Displaying the possible expected values:
		   System.out.println("These are the expected utilities:");
		   for (int i = 0; i < net.getOutcomeCount(nodeDecision); i++) {
		     String parentOutcomeId = net.getOutcomeId(nodeDecision, i);
		     double expectedUtility = net.getNodeValue("Gain")[i];
		     
		     System.out.print("  - \"" + decisionName + "\" = " + parentOutcomeId + ": ");
		     System.out.println("Expected Utility = " + expectedUtility);
		   }
		 }
		 catch (SMILEException e) {
		   System.out.println(e.getMessage());
		 }
		}
	public void ComputeValueOfInformation() {
		 try {
		   Network net = new Network();
		   net.readFile("models/MovieModel.xdsl");
		   
		   ValueOfInfo voi = new ValueOfInfo(net);
		   
		   // Getting the handles of nodes "Forecast" and "Invest":
		   net.getNode("Forecast");
		   net.getNode("Invest");
		   
		   voi.addNode("Forecast");
		   voi.setDecision("Invest");
		   voi.update();
		   
		   double[] results = voi.getValues();
		   double EVIForecast = results[0];
		   
		   System.out.println("Expected Value of Information (\"Forecast\") = " + EVIForecast);
		 }
		 catch (SMILEException e) {
		   System.out.println(e.getMessage());
		 }
		}

}
