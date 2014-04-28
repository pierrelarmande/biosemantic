package com.wscreation.test;

import java.io.File;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.wscreation.d2rqmappingfile.Writter;

public class TestWritter {


	public static void main(String[] args) {
		String mappingPath="mapping-TG_LOCAL_RICE.n3";
		Model model=ModelFactory.createDefaultModel();
//		ModelD2RQ model=new ModelD2RQ(mappingPath);
		model.read(new File(mappingPath).toURI().toString(),"N3");
		Writter writter=new Writter(model,null);
	}

}
