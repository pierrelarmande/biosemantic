package com.wscreation.d2rqmappingfile;

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Resource;

public class ForeignKeys {
	
	private Vector<String> join=new Vector<String>();
	private Resource table;
	private Resource database;
	
	
	public ForeignKeys(Vector<String> join,Resource table, Resource db) {
		this.join=join;
		this.table=table;
		this.database=db;
	}
	
	public ForeignKeys(String join,Resource table, Resource db){
		this.join.add(join);
		this.table=table;
		this.database=db;
	}
	
	public ForeignKeys(){}
	
	public Vector<String> getJoin(){return join;}
	public Resource getTable(){return table;}
	public Resource getDatabase(){return database;}
	public void setOneJoin(String join){
		this.join.add(join);
	}

}
