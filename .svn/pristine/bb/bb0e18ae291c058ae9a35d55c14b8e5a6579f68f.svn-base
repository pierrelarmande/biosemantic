package com.wscreation.main;

import java.io.File;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.wscreation.d2rqmappingfile.AddRelationships;
import com.wscreation.d2rqmappingfile.ParseD2RQ;
import com.wscreation.d2rqmappingfile.Writter;

public class Compatible_test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File outFile=new File("mapping_return.n3");
		
		Model model=ModelFactory.createDefaultModel();
		model.read(new File("mapping_test.n3").toURI().toString(),"N3");
		AddRelationships wr=new AddRelationships(model);
		model=wr.addRelationships();
		Writter writter=new Writter(model,outFile);
	}

}
