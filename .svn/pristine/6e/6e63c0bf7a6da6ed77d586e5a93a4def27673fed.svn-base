package com.wscreation.findpath;


import com.hp.hpl.jena.rdf.model.Resource;

/** a Table Object contains informations about a table of the mapping file
 * 
 * @author WOLLBRETT
 *
 */
public class Table {
	public Resource tableName;
	public double tableWeight;
	boolean DEBUG=true;
	
	public Table(Resource tableName){
		this.tableName=tableName;
		tableWeight=-1;
	}
	
	public Resource getTableResource(){return tableName;}
	public double getNodeWeight(){return tableWeight;}
	public void setTableResource(Resource resourceName){tableName=resourceName;}
	public void setTableWeight(double weight){tableWeight=weight;}
	
	public String toString() {
		return "Resource: "+tableName+"weight: "+tableWeight;
	}
}
