package com.wscreation.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.wscreation.d2rqmappingfile.ForeignKeys;
import com.wscreation.d2rqmappingfile.PrimaryKeys;
import com.wscreation.findpath.Parcours_d2rq;

public class FindHeritageTest {
	public static void main(String[] args) {
//		String mappingPath="mapping-TG_LOCAL_RICE.n3";
//		String mappingPath="mapping_entier.n3";
		String mappingPath="mapping-TG_LOCAL_RICE_test2.n3";
		Model model=ModelFactory.createDefaultModel();
//		ModelD2RQ model=new ModelD2RQ(mappingPath);
		model.read(new File(mappingPath).toURI().toString(),"N3");
		Vector<PrimaryKeys > primaryKeys=new Vector<PrimaryKeys>();
		Vector<ForeignKeys> foreignKeys=new Vector<ForeignKeys>();
		Parcours_d2rq d2rq=new Parcours_d2rq(mappingPath);
		Model modelToParse=d2rq.getModelToParse();
		StmtIterator stmts=modelToParse.listStatements();
		//parcours tous les statements du modèle
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			
			if(stmt.getPredicate().getLocalName().equals("join")){
				Resource column=stmt.getSubject();
				Vector<Statement> columnInfos=d2rq.infosSurUneColonne(column);
				Resource table=null;
				Resource database=null;
				String join="";
				//parcours toutes les infos sur une colonne contenant une clé étrangère
				for (int i=0;i<columnInfos.size();i++){
					if(columnInfos.elementAt(i).getPredicate().getLocalName().equals("belongsToClassMap")){
						table=(Resource)columnInfos.elementAt(i).getObject();
						database=d2rq.refersToDatabase((Resource)columnInfos.elementAt(i).getObject());
					}
					else if(columnInfos.elementAt(i).getPredicate().getLocalName().equals("join")){
						join=columnInfos.elementAt(i).getObject().toString();
					}
					
				}
				//booléen permettant de savoir si une table déjà enregistrée est identique à celle trouvée
				boolean find=false;
				for (int i=0;i<foreignKeys.size();i++){
					Resource currentTable=foreignKeys.elementAt(i).getTable();
					Resource currentDB=foreignKeys.elementAt(i).getDatabase();
					if(currentDB.equals(database)&&currentTable.equals(table)){
						foreignKeys.elementAt(i).setOneJoin(join);
						find=true;
					}
				}
				if(find==false){
					foreignKeys.add(new ForeignKeys(join, table, database));
				}
			}
			
			if(stmt.getPredicate().getLocalName().equals("dataStorage")){
//				System.out.println(stmt.getObject());
				Resource table=stmt.getSubject();
				Resource database=(Resource)stmt.getObject();
				String primaryKey="";
				Vector<Statement> tableInfos =d2rq.infosSurUneTable(table);
				//parcours tous les statements d'une table
				for (int i=0;i<tableInfos.size();i++){
//					
//					if(tableInfos.elementAt(i).getPredicate().getLocalName().equals("refersToClassMap")){
////						System.out.println(tableInfos.elementAt(i));
//					}
					//récupère la clé primaire de la table
					if(tableInfos.elementAt(i).getPredicate().getLocalName().equals("uriPattern")){
						primaryKey=tableInfos.elementAt(i).getObject().toString();
					}
				}
				primaryKeys.add(new PrimaryKeys(primaryKey, table, database));
			}	
		}
		//parcours le vecteur de clé primaire
		for(int i=0;i<primaryKeys.size();i++){
			boolean equal=false;
			Resource database=primaryKeys.elementAt(i).getDatabase();
			Vector<String> vectorPK=primaryKeys.elementAt(i).getUriPattern();
			for(int j=0;j<foreignKeys.size();j++){
				//vérifie que les 2 tables sont dans la même base de données
				if(foreignKeys.elementAt(j).getDatabase().equals(database)){
					Vector<String> vectorFK=foreignKeys.elementAt(j).getJoin();
					if(vectorFK.size()==vectorPK.size()){
						for(int k=0;k<vectorPK.size();k++){
							equal=false;
//							System.out.println();
							for(int l=0;l<vectorFK.size();l++){
								String fk=vectorFK.elementAt(l).split(" = ")[1];
								if(fk.equals(vectorPK.elementAt(k))){
									equal=true;
									l=vectorFK.size();
								}
//								System.out.println(vectorPK.elementAt(k)+" -> "+vectorFK.elementAt(l));
							}
							if(equal==false){
								k=vectorPK.size();
							}
						}
						if(equal==true){
							System.out.println("table parent: "+primaryKeys.elementAt(i).getTable().getLocalName()+"\t table héritée: "+foreignKeys.elementAt(j).getTable().getLocalName());
							Property predicate=new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#subClassOf");
							Statement heritage=new StatementImpl(foreignKeys.elementAt(j).getTable(),predicate, (RDFNode)primaryKeys.elementAt(i).getTable());
//							System.out.println("table parent: "+primaryKeys.elementAt(i).getTable().getLocalName()+"\ttable héritée: "+foreignKeys.elementAt(j).getTable().getLocalName());
							model.add(heritage);
						}
					}
//					foreignKeys.
				}
			}
		}
		File outFile=new File("mapping_heritage.n3");
		OutputStream out=null;
		try{
			outFile.createNewFile();
			out=new FileOutputStream(outFile);
		}
		catch(FileNotFoundException e){System.out.println(e.getMessage());}
		catch(IOException e){System.out.println(e.getMessage());}
		model.write(out,"N3");
	}
}
