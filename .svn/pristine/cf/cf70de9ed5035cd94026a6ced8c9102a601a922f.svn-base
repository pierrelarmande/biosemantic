package com.wscreation.findpath;

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class FindSomePaths {
	
	//ATTRIBUTS
	private Resource[] inputColumns;
	private Resource[] outputColumns;
	public Parcours_d2rq parcours;
//	private Vector<PathNode> path=new Vector<PathNode>();
//	private int lengthOfThePath=0;
	private String mappingFile="";
	private Model model=null;
	
	public void setInputColumn(Resource[] input){this.inputColumns=input;}
	public void setOutputColumn (Resource[] output){this.outputColumns=output;}
	public void setMappingFile (String mappingFile){this.mappingFile=mappingFile;}
	public Resource[] getInputConlumn(){return inputColumns;}
	public Resource[] getOutputColumn(){return outputColumns;}
	public String getMappingFile(){return mappingFile;}
	public Model getModel(){return model;}
		
	//CONSTRUCTOR
	public FindSomePaths(String mappingFile){
		parcours=new Parcours_d2rq(mappingFile);
		model=parcours.getModelToParse();
	}
	
	public FindSomePaths(String mappingFile,Resource[] input, Resource[] output){
		this.inputColumns=input;
		this.outputColumns=output;
		parcours=new Parcours_d2rq(mappingFile);
		model=parcours.getModelToParse();
	}
	
	public Vector<PathNode> findSomePaths(){
		Vector<PathNode> path=new Vector<PathNode>();
//		int numberOfPaths=inputColumns.length*outputColumns.length;
//		Vector<Vector<PathNode>> allpaths=new Vector<Vector<PathNode>>();
		for(int i=0;i<inputColumns.length;i++){
//			Resource input=inputColumns[i];
			for(int j=0;j<outputColumns.length;j++){
				
			}
		}
		
		return path;
	}

}
