package com.wscreation.d2rqmappingfile;

import java.io.File;
import com.hp.hpl.jena.rdf.model.Model;
import de.fuberlin.wiwiss.d2rq.mapgen.MappingGenerator;
import de.fuberlin.wiwiss.d2rq.sql.ConnectedDB;

public class CreateMappingFile {
	
	/**
	 * constructor for the creation of the mapping file
	 * @param parameters
	 */
	public CreateMappingFile(D2rqParamaters parameters){
		//D2RQ 0.7
		MappingGenerator mapgen=new MappingGenerator(parameters.jdbcURL);
//		ConnectedDB db=new ConnectedDB(parameters.jdbcURL, parameters.user, parameters.password);
//		MappingGenerator mapgen=new MappingGenerator(db);
		//D2RQ 0.7
		mapgen.setDatabasePassword(parameters.password);
		mapgen.setDatabaseUser(parameters.user);
		mapgen.setInstanceNamespaceURI(parameters.baseURI);
		parameters.driverClass=findDriverClass(parameters);
		mapgen.setJDBCDriverClass(parameters.driverClass);
		File outFile=new File(parameters.outFile);
		Model model=mapgen.mappingModel(parameters.baseURI, null);
		AddRelationships wr=new AddRelationships(model);
		model=wr.addRelationships();
		new Writter(model,outFile);
	}
	
	public String findDriverClass(D2rqParamaters parameters) {
		String typeDeDriver=parameters.typeDeDriver;
		if(typeDeDriver!=null){
			if(typeDeDriver.toString().equals("mysql")){
				return "com.mysql.jdbc.Driver";
			}
			else if(typeDeDriver.toString().equals("postgresql")){
				System.out.println("pg "+typeDeDriver);
				return "org.postgresql.Driver";
			}
			else if(typeDeDriver.toString().equals("oracle")){
				System.out.println("or "+typeDeDriver);
				return "oracle.jdbc.OracleDriver";
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}
}
