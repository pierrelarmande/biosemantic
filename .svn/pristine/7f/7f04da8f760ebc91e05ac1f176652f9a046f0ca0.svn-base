package com.wscreation.test;

import java.io.File;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.ibm.icu.math.BigDecimal;
import com.wscreation.findpath.Parcours_d2rq;


public class Test{
	
	public static void main(String[] args) {
		String mappingPath="mapping-TG_LOMAGNE_RICE_final.n3";
		Model model=ModelFactory.createDefaultModel();
		double value1=0.96;
		double value2=1.93;
		double blop=new BigDecimal(value1).add(new BigDecimal(value2)).doubleValue();
		System.out.println((blop));
		
//		ModelD2RQ model=new ModelD2RQ(mappingPath);
		model.read(new File(mappingPath).toURI().toString(),"N3");
		Parcours_d2rq parcours=new Parcours_d2rq(model);
		StmtIterator stmts=model.listStatements();
//		while(stmts.hasNext()){
//			Statement stmt=stmts.next();
//			if(stmt.getSubject().getLocalName().equals("aflp_id_aflp")){
//				System.out.println(stmt);//.getPredicate().getLocalName());
//			}
//		}
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
//			System.out.println(stmt);
			if(stmt.getPredicate().getLocalName().equals("AssociatedTo")){
//				System.out.println(stmt);
				Resource tableName=stmt.getSubject();
//				System.out.println(tableName.getLocalName());
				StmtIterator stmts2=model.listStatements();
//				while(stmts2.hasNext()){
//					Statement stmt2=stmts2.next();
//					if(stmt2.getSubject().equals(tableName)&&stmt2.getPredicate().getLocalName().equals("Arity")){
//						System.out.println(tableName);
//						Vector<Statement> tableInfos=parcours.infosSurUneTable(tableName);
//						for (int i=0;i<tableInfos.size();i++){
//							System.out.println(tableInfos.elementAt(i));
//						}
//					}
//				}
			}
			
		}
	}
}