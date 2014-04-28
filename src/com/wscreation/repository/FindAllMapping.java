package com.wscreation.repository;

import java.io.File;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

/**
 * 
 * @author WOLLBRETT
 *
 */
public class FindAllMapping {
	
	private String mappingDirectory;
	private Resource input;
	private Resource output;
	private Vector<String> annotatedMappings;
	private boolean	DEBUG=false;
	
	//CONSTRUCTOR
	FindAllMapping(String mappingDirectory, Resource input, Resource output){
		this.mappingDirectory=mappingDirectory;
		this.input=input;
		this.output=output;
		findAnnotatedMappings();
	}
	
	//ACCESSOR
	public Vector<String> getAnnotatedMappings(){return annotatedMappings;}
	
	/**retrieve mapping files of the repository annotated with both input and output concept
	 * 
	 */
	private void findAnnotatedMappings(){
		annotatedMappings=new Vector<String>();
		File mappingDir=new File(mappingDirectory);
		File [] files=mappingDir.listFiles();
		for(int i=0;i<files.length;i++){
			if(files[i].isFile()){
				boolean findInput=false;
				boolean findOutput=false;
				//si le fichier est un fichier N3
				String fileName=files[i].getName();
				String filePath=files[i].toString();
				if(DEBUG==true){
					System.out.println(fileName);
				}
				if(fileName.endsWith("N3")||fileName.endsWith("n3")){
					Model model=ModelFactory.createDefaultModel();
					model.read(new File(filePath).toURI().toString(),"N3");
					StmtIterator stmts=model.listStatements();
					while(stmts.hasNext()){
						Statement stmt=stmts.next();
						if(stmt.getObject().isResource()){
							Resource object=(Resource)stmt.getObject();
							if(object.equals(input)){
								if(DEBUG==true){
									System.err.println("trouvé input "+input.getLocalName());
								}
								findInput=true;
							}
							else if(object.equals(output)){
								findOutput=true;
								if(DEBUG==true){
									System.err.println("trouvé output "+output.getLocalName());
								}
							}
						}
					}
					if(findOutput==true&&findInput==true){
						annotatedMappings.add(files[i].toString());
					}
					else{
						if(DEBUG==true){
							System.out.println("ce fichier de mapping n'est pas annoté avec les concepts d'entrée et de sortie");
						}
					}
				}
			}
		}
		
	}

}
