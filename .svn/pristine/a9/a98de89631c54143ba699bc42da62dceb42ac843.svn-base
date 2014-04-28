package com.wscreation.findpath;

import com.hp.hpl.jena.rdf.model.Resource;

public class AssociationWithoutAttributs {
	
	private Resource refersTable=null;
	private Resource refersColumn=null;
	private Resource belongsTable=null;
	private Resource belongsColumn=null;
	private Resource table=null;
	
	public AssociationWithoutAttributs(Resource refT,Resource refC,Resource belT,Resource belC,Resource table) {
		this.refersTable=refT;
		this.belongsTable=belT;
		this.belongsColumn=belC;
		this.refersColumn=refC;
		this.table=table;
	}
	
	public Resource getRefersTable(){return refersTable;}
	public Resource getBelongsTable(){return belongsTable;}
	public Resource getBelongsColumn(){return belongsColumn;}
	public Resource getRefersColumn(){return refersColumn;}
	public Resource getTable(){return table;}

}
