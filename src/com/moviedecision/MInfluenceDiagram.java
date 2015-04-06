package com.moviedecision;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smile.Network;

public class MInfluenceDiagram {
	 public void CreateNetwork() {
		  try {
		    Network net = new Network();
		    
		    // Creating node "Success" and setting/adding outcomes:
		    net.addNode(Network.NodeType.Cpt, "Success");
		    net.setOutcomeId("Success", 0, "Success");
		    net.setOutcomeId("Success", 1, "Failure");
		    
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
		    net.writeFile("tutorial_a.xdsl");
		  }
		  catch (SMILEException e) {
		    System.out.println(e.getMessage());
		  }
		 }


}
