package com.wscreation.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.wscreation.findpath.Parcours_d2rq;

public class FindArityTest {
	public static void main(String[] args) {
		String mappingPath="blop.n3";
		Model model=ModelFactory.createDefaultModel();
//		ModelD2RQ model=new ModelD2RQ(mappingPath);
		model.read(new File(mappingPath).toURI().toString(),"N3");
		Parcours_d2rq d2rq=new Parcours_d2rq(model);
		Model modelToParse=d2rq.getModelToParse();
		StmtIterator stmts=modelToParse.listStatements();
		//parcours tous les statements du modèle
		Map<Resource, Integer> tableArity=new HashMap<Resource, Integer>();
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getPredicate().getLocalName().equals("join")){
//				System.out.println(stmt.getSubject().getLocalName());
				Vector<Statement> columns=d2rq.infosSurUneColonne(stmt.getSubject());
				for (int i=0;i<columns.size();i++){
					Statement column=columns.elementAt(i);
//					System.out.println(column);
					if(column.getPredicate().getLocalName().equals("belongsToClassMap")){
						if(tableArity.containsKey((Resource)column.getObject())){
							int arity=tableArity.get((Resource)column.getObject());
							tableArity.put((Resource)column.getObject(), arity+1);
						}
						else{
							tableArity.put((Resource)column.getObject(), 1);
						}
					}
//					else if(column.getPredicate().getLocalName().equals("refersToClassMap")){
//						if(tableArity.containsKey((Resource)column.getObject())){
//							int arity=tableArity.get((Resource)column.getObject());
//							tableArity.put((Resource)column.getObject(), arity+1);
//						}
//						else{
//							tableArity.put((Resource)column.getObject(), 1);
//						}
//					}
				}
			}
		}
		for(Entry<Resource, Integer> entry : tableArity.entrySet()) {
		    Resource cle = entry.getKey();
		    int valeur = entry.getValue();
		    if(valeur>1){
		    	System.out.println(cle.getLocalName()+"  ->  "+valeur);
		    // traitements
		    }
		}
	}

}
