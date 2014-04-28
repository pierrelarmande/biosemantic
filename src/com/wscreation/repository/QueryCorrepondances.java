package com.wscreation.repository;

import java.util.Vector;

public class QueryCorrepondances {
	
	//ATTRIBUTS
	private Vector<String> query;
	private String mappingFile;
	
	//CONSTRUCTOR
	QueryCorrepondances(Vector<String> query, String mappingFile){
		this.query=query;
		this.mappingFile=mappingFile;
	}
	
	//ACCESSOR
	public Vector<String> getQuery(){return query;}
	public String getMappingFile(){return mappingFile;}
	
}
