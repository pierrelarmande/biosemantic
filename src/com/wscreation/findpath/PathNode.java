package com.wscreation.findpath;

import com.hp.hpl.jena.rdf.model.Resource;
/**
 * class used to create a PathNode object
 * This object correspond to a node of the path detected in the FindOnePath class
 * @author WOLLBRETT
 *
 */
public class PathNode {
	
	public static int ISTHEINPUTANNOTATION=0;
	public static int ISATABLE=1;
	public static int ISASIMPLECOLUMN=2;
	public static int ISAPRIMARYKEYCOLUMN=3;
	public static int ISAFOREIGNKEYCOLUMN=4;
	public static int ISANINVERSEFOREIGNKEYCOLUMN=5;
	public static int ISTHEOUTPUTANNOTATION=6;
	public static int ISHERITAGERELATION=1;
	public static int ISASSOCIATIONRELATION=0;
	public static int ISASSOCIATIONTABLE=2;
	public static int ISANASSOCIATIONTABLEWITHOUTATTRIBUTS=3;
	private Resource subjectNode=null;
	private Resource objectNode=null;
	private double weight=-1;
	private int nodeType=-1;
	private int relationType=-1;
	
	public Resource getSubjectNode(){return subjectNode;}
	public Resource getObjectNode(){return objectNode;}
	public double getWeight(){return weight;}
	public int getNodeType(){return nodeType;}
	public int getRelationType(){return relationType;}
	public void setRelationType(int relationType){this.relationType=relationType;}
	public void setSubjectNode(Resource sNode){this.subjectNode=sNode;}
	public void setObjectNode(Resource oNode){this.objectNode=oNode;}
	public void setWeight(double weight){this.weight=weight;}
	public void setNodeType(int type){this.nodeType=type;}
	
	PathNode(Resource subjectNode, Resource objectnode,double weight,int nodeType, int relationType){
		this.subjectNode=subjectNode;
		this.objectNode=objectnode;
		this.weight=weight;
		this.nodeType=nodeType;
		this.relationType=relationType;
	}
	
	PathNode(Resource node,double weight,int nodeType){
		this.objectNode=node;
		this.weight=weight;
		this.nodeType=nodeType;
	}
	
	PathNode(Resource subjectNode, Resource objectnode,double weight,int nodeType){
		this.subjectNode=subjectNode;
		this.objectNode=objectnode;
		this.weight=weight;
		this.nodeType=nodeType;
	}
	
	PathNode(Resource node,double weight,int nodeType,int relationType){
		this.objectNode=node;
		this.weight=weight;
		this.nodeType=nodeType;
		this.relationType=relationType;
	}
	
	PathNode(){}
	
	public boolean isATable(){
		if(nodeType==1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isASimpleColumn(){
		if(nodeType==2){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isAPrimaryKeyColumn(){
		if(nodeType==3){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isAForeignKeyColumn(){
		if(nodeType==4){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isASimpleAssociationRelation(){
		if(relationType==0){
			return true;
		}
		else{
			return false;
		}
	}
		
	public boolean isAnAssociationTableRelation(){
		if(relationType==2){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isAHeritageRelation(){
		if(relationType==1){
			return true;
		}
		else{
			return false;
		}
	}
	

	public String toString() {
		return subjectNode+"\t"+objectNode+"\t"+weight+"\t"+nodeType+"\t"+relationType;
	}
}

