package com.wscreation.d2rqmappingfile;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class AddRelationships {
	Model model=ModelFactory.createDefaultModel();
	
	public AddRelationships(Model model) {
		this.model=model;
	}
	
	/**add all the kind of previously detected relationships to the mapping file
	 * 
	 * @return
	 */
	public Model addRelationships(){
		AddHeritageRelationships herRel=new AddHeritageRelationships(model);
		model=herRel.getModel();
		AddAssociationRelationships assRel=new AddAssociationRelationships(model);
		model=assRel.getModel();
		AddArity arity=new AddArity(model);
		model=arity.getModel();
		return model;
	}
}
