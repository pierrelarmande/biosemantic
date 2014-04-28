package com.wscreation.d2rqmappingfile;

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Resource;

public class PrimaryKeys {

	private Vector<String> uriPattern;
	private Resource table;
	private Resource database;
	
	
	public PrimaryKeys(String uriPattern,Resource table, Resource db) {
		String[]split=uriPattern.split("@@");
		
		this.uriPattern=new Vector<String>();
		for(int j=1;j<split.length;j++){
			if(!split[j].equals("/")){
				this.uriPattern.add(split[j]);
			}
			
		}
		this.table=table;
		this.database=db;
	}
	
	public Vector<String> getUriPattern(){return uriPattern;}
	public Resource getTable(){return table;}
	public Resource getDatabase(){return database;}
	
	
}
