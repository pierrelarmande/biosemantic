package com.wscreation.main;

import java.io.File;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.wscreation.d2rqmappingfile.AddAssociationRelationships;
import com.wscreation.d2rqmappingfile.CreateMappingFile;
import com.wscreation.d2rqmappingfile.D2rqParamaters;

import de.fuberlin.wiwiss.d2rq.mapgen.MappingGenerator;

public class Testd2rq {
	
	public static void main(String[] args) {
//		D2rqParamaters param=new D2rqParamaters("tropgene_user","jules12","mysql","http://lomagne.cirad.fr","jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE","mapping-TG_LOMAGNE_RICE_test2.n3");
//		D2rqParamaters param=new D2rqParamaters("jwollbrett","yahooal","mysql","http://localhost:2020","jdbc:mysql://127.0.0.1/TROPGENE_RICE","mapping-TG_LOCAL_RICE_test2.n3");
//		D2rqParamaters param=new D2rqParamaters("odb_user","mlk25db","mysql","http://medoc.cirad.fr:3306","jdbc:mysql://medoc.cirad.fr/odb_japonica_public","mapping-ODB_test_2.n3");
//		System.out.println("guest"+" "+"guest"+" "+"PostgreSQL"+" "+"http://medoc.cirad.fr"+" "+"jdbc:postgresql://medoc.cirad.fr/gnpannot_musa"+" "+"mapping-GNP.n3");
		D2rqParamaters param=new D2rqParamaters("guest","guest","postgresql","http://medoc.cirad.fr","jdbc:postgresql://medoc.cirad.fr/gnpannot_musa","D:\\workspace2\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\BioSemantic_webapp\\WEB-INF\\tmp\\mapping-MEDOC-GNPANNOT_MUSA.n3");
//		MappingGenerator mapgen=new MappingGenerator(param.jdbcURL);
//		Model model=mapgen.mappingModel(param.baseURI, null);
		new CreateMappingFile(param);
//		String mappingPath="mapping_entier.n3";
//		Model model=ModelFactory.createDefaultModel();
//		model.read(new File(mappingPath).toURI().toString(),"N3");
//		AddAssociationRelationships aar=new AddAssociationRelationships(model);
	}
}
