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


public class MInfluenceDiagramJ {
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
		   
		   net.addNode(Network.NodeType.Cpt, "Offset");
		   net.setOutcomeId("Offset", 1, "accurate");
		   net.setOutcomeId("Offset", 1, "mid");
		   net.setOutcomeId("Offset", 1, "noEffect");
		   
		   net.addNode(Network.NodeType.Cpt, "Youtube_Trailer_View_Count");
		   net.setOutcomeId("Youtube_Trailer_View_Count", 1, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Facebook");
		   net.setOutcomeId("Facebook", 1, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Google_Result");
		   net.setOutcomeId("Google_Result", 1, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Twitter");
		   net.setOutcomeId("Twitter", 1, "value");
		   
		   net.addNode(Network.NodeType.Cpt, "Social_Network_Activeness");
		   net.setOutcomeId("Social_Network_Activeness", 1, "value");

		   net.addNode(Network.NodeType.Cpt, "Box_Office");
		   net.setOutcomeId("Box_Office", 1, "normal");
		   net.setOutcomeId("Box_Office", 1, "threeD");
		   net.setOutcomeId("Box_Office", 1, "iMAX");
		   net.setOutcomeId("Box_Office", 1, "fourK");
		   
		   net.addNode(Network.NodeType.Cpt, "Holiday_Preference");
		   net.setOutcomeId("Holiday_Preference", 1, "holiday");
		   
		   net.addNode(Network.NodeType.Cpt, "Risks");
		   net.setOutcomeId("Risks", 1, "yes");
		   net.setOutcomeId("Risks", 1, "no");
		   
		   net.addNode(Network.NodeType.Cpt, "Gross_Profit");
		   net.setOutcomeId("Gross_Profit", 1, "normal");
		   net.setOutcomeId("Gross_Profit", 1, "threeD");
		   net.setOutcomeId("Gross_Profit", 1, "iMAX");
		   net.setOutcomeId("Gross_Profit", 1, "fourK");
		   
		   net.addNode(Network.NodeType.Cpt, "Lease_Price");
		   net.setOutcomeId("Lease_Price", 1, "normal");
		   net.setOutcomeId("Lease_Price", 1, "threeD");
		   net.setOutcomeId("Lease_Price", 1, "iMAX");
		   net.setOutcomeId("Lease_Price", 1, "fourK");
		   
		   net.addNode(Network.NodeType.Cpt, "profit");
		   net.setOutcomeId("profit", 1, "value");
		
		   //之后的没改
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
		   net.setNodePosition("Success", 20, 20, 80, 30);
		   net.setNodeBgColor("Success", Color.red);
		   net.setNodeTextColor("Success", Color.white);
		   net.setNodeBorderColor("Success", Color.black);
		   net.setNodeBorderWidth("Success", 2);
		   net.setNodePosition("Forecast", 30, 100, 60, 30);
		   
		   // Writting the network to a file:
		   net.writeFile("models/MovieModel.xdsl");
		 }
		 catch (SMILEException e) {
		   System.out.println(e.getMessage());
		 }
		}
	//之前的没改
	
	public void InferenceWithBayesianNetwork() {
		 try {
		   Network net = new Network();
		   net.readFile("models/changeID.xdsl"); 
		   // Updating the network: 
		   net.updateBeliefs();
		       
		   // ---- We want to compute P("Forecast" = Moderate) ----
		   
		   // Getting the handle of the node "Forecast":
		   net.getNode("profit");
		 }
		 catch (SMILEException e) {
		   System.out.println(e.getMessage());
		 }
		}
	
	public static void main(String[] args) { 
		
	}
}
       
