package com.wscreation.repository;

import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.wscreation.createrequest.CreateRequest;
import com.wscreation.findpath.FindOnePath;
import com.wscreation.findpath.PathNode;

/**
 * create a query for all detected mapping files
 * @author WOLLBRETT
 *
 */
public class FindQueryForAllMappingFiles {
	
	private Vector<QueryCorrepondances> queryCorres=new Vector<QueryCorrepondances>();
	
	public FindQueryForAllMappingFiles(String mappingDirectory,Resource inputR,Resource outputR) {
		FindAllMapping fam=new FindAllMapping(mappingDirectory, inputR, outputR);
		Vector<String> mappingFiles=fam.getAnnotatedMappings();
		for(int i=0;i<mappingFiles.size();i++){
			FindOnePath path=new FindOnePath(mappingFiles.elementAt(i));
			//recherche tous les elements du schéma annotés avec le concept d'entrée
			Vector<Statement> stmts=path.parcours.retrieveColumnWithThisAnnotation(inputR);
			//recherche tous les elements du schéma annotés avec le concept de sortie
			Vector<Statement> stmts2=path.parcours.retrieveColumnWithThisAnnotation(outputR);
			if(stmts.size()>0&&stmts2.size()>0){
				
				if(stmts.size()==1&&stmts2.size()==1){
					Resource input=stmts.elementAt(0).getSubject();
					path.setInputColumn(input);
					Resource output=stmts2.elementAt(0).getSubject();
					path.setOutputColumn(output);
					Vector<PathNode> pathTree=path.findOnePath(false,null);
					//création de la requête
					if(!(pathTree==null)){
						CreateRequest cr=new CreateRequest(mappingFiles.elementAt(i),pathTree, input, output,inputR,outputR,path);
						Vector<String> stn=cr.getRequestPath();
						queryCorres.add(new QueryCorrepondances(stn, mappingFiles.elementAt(i)));
					}
					else{
						queryCorres.add(new QueryCorrepondances(new Vector<String>(), mappingFiles.elementAt(i)));
					}
				}
				else{
					System.out.println("no query for the mapping file "+mappingFiles.elementAt(i)+"." +
							"\nCause by the number of elements annotated with the input and/or output concept");
				}
			}
			else{
				System.out.println("no query for the mapping file "+mappingFiles.elementAt(i)+".");
			}
		}
	}
	public Vector<QueryCorrepondances> getQueryCorres(){return queryCorres;}
}
