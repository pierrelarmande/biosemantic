package com.wscreation.findpath;

import java.util.Vector;

import javax.swing.text.TableView;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * class detecting all paths linking one input to one output
 * @author WOLLBRETT
 *
 */
public class FindOnePath {
	
	private Resource inputColumn;
	private Resource outputColumn;
	public Parcours_d2rq parcours;
	private Vector<PathNode> path=new Vector<PathNode>();
	private double lengthOfThePath=0;
	private String mappingFile="";
	private Model model=null;
	private Resource outputTable;
	private Vector<AssociationWithoutAttributs> awa;
	
	
	//ACCESS
	public void setInputColumn(Resource input){this.inputColumn=input;}
	public void setOutputColumn (Resource output){this.outputColumn=output;}
	public void setMappingFile (String mappingFile){this.mappingFile=mappingFile;}
	public Resource getInputConlumn(){return inputColumn;}
	public Resource getOutputColumn(){return outputColumn;}
	public String getMappingFile(){return mappingFile;}
	public Model getModel(){return model;}
		
	//CONSTRUCTOR
	public FindOnePath(String mappingFile){
		parcours=new Parcours_d2rq(mappingFile);
		model=parcours.getModel();
		awa=parcours.getAssociationWithoutAttributs();
	}
	
	public FindOnePath(String mappingFile,Resource input, Resource output){
		this.inputColumn=input;
		this.outputColumn=output;
		parcours=new Parcours_d2rq(mappingFile);
		model=parcours.getModel();
		awa=parcours.getAssociationWithoutAttributs();
	}
		
	//METHODS
	
	/** find all paths linking selected input to selected output
	 * @return Vector<PathNode> vector containing the path
	 */
	public Vector<PathNode> findOnePath(boolean deleteATable, Resource tableToDelete){
		Vector<Table> tablesVector=parcours.getTables();
		boolean trouve=false;
		if(deleteATable==true){
			tablesVector=deleteTheTable(tablesVector, tableToDelete);
		}
		//ok ,toutes les tables sont dans le vecteur
		Resource table=parcours.columnIsInTable(inputColumn);
//		System.out.println("inco"+" "+inputColumn+" "+table);
		outputTable=parcours.columnIsInTable(outputColumn);
		Resource inputAnnotation=findAnnotation(inputColumn);
		path.add(new PathNode(null,inputAnnotation,lengthOfThePath,PathNode.ISTHEINPUTANNOTATION));
		path.add(new PathNode(null,inputColumn,lengthOfThePath,PathNode.ISASIMPLECOLUMN));
		boolean previousnodeIsOutput=false;
		//add statement to the path
		tablesVector=editTableWeight(tablesVector, table, lengthOfThePath);
		int tailleTablesVectorPrec=tablesVector.size()+1;
		while(!tablesVector.isEmpty()){
			if(!(tailleTablesVectorPrec>tablesVector.size())){
//				System.out.println(tailleTablesVectorPrec+" -> "+tablesVector.size());
				tablesVector.clear();
			}
//			else{
//				tailleTablesVectorPrec--;
//			}
			boolean findSomething=false;
			//récupère statements sur table de plus court chemin, edit également la variable lengthOfThePath à la valeur de path de la table
			Vector<Statement> smallestPathTableStatements=tableWithSmallestPath(tablesVector);
			//vérifie que des statements sont associés à la table de plus court chemin
			if(!smallestPathTableStatements.isEmpty()){
				//récupère la Resource correspondant à la table de plus court chemin
				table=parcours.getTableResource(smallestPathTableStatements);
				//supprime la table de plus court chemin de la liste des tables non parcourues
				if(!table.equals(outputTable)){
					tablesVector=deleteTheTable(tablesVector,table);
					previousnodeIsOutput=false;
				}
				else{
					trouve=true;
				}
				//weight prend la valeur de l'arité de l'association(nbre de clés étrangères de  l'objet "table" vers d'autres tables
//				int weight=parcours.getArity(table);
				//récupère la clé primaire de l'objet "table"
				Vector <Resource> primaryKey=findPrimaryKey(smallestPathTableStatements);
				//vérifie que la table possède bien une clé primaire
				if(primaryKey.isEmpty()){
					System.err.println("pas de clé primaire");
					System.exit(0);
				}
				else {
					for(int i=0;i<primaryKey.size();i++){
						path.add(new PathNode(null,primaryKey.elementAt(i), lengthOfThePath, PathNode.ISAPRIMARYKEYCOLUMN));
					}
					findSomething=true;
				}
				//si on se trouve sur la table contenant l'élément de départ
				if(lengthOfThePath-1==0){
					path.add(new PathNode(null,table,lengthOfThePath,PathNode.ISATABLE,-1));
				}
				//si on ne se trouve pas sur la table contenant l'élément de départ
				else{
					//find the kind of relation and add node to the path
					if(isHeritageRelation(table)){
						path.add(new PathNode(table,lengthOfThePath,PathNode.ISATABLE,PathNode.ISHERITAGERELATION));
					}
					else if(isAssociationTableRelation(table)){
						path.add(new PathNode(table,lengthOfThePath,PathNode.ISATABLE,PathNode.ISASSOCIATIONTABLE));
					}
					else{
//						System.out.println(table);
						path.add(new PathNode(table,lengthOfThePath,PathNode.ISATABLE,PathNode.ISASSOCIATIONRELATION));
					}
					
				}
				//parcours tous les statements de la table à la recherche de l'élément de sortie
				for(int i=0;i<smallestPathTableStatements.size();i++){
					if(smallestPathTableStatements.elementAt(i).getSubject().equals(outputColumn)){
						path.add(new PathNode(outputColumn,lengthOfThePath-1,PathNode.ISASIMPLECOLUMN));
						Resource output=findAnnotation(outputColumn);
						path.add(new PathNode(output,lengthOfThePath-1,PathNode.ISTHEOUTPUTANNOTATION));
						i=smallestPathTableStatements.size();
						findSomething=true;
					}
				}
				//récupère les clés étrangères de la table
				Vector<Resource> foreignKeys=findForeignKeys(table);
				//vérifie qu'il y a bien des clés étrangères
				if(!table.equals(outputTable)){
				if(!foreignKeys.isEmpty()&&!tablesVector.isEmpty()){
					//parcours les clés étrangères
					for(int i=0;i<foreignKeys.size();i++){
						//récupère info sur la table reliée par la clé étrangère
						Resource tableEtrangere=parcours.columnIsInTable(foreignKeys.elementAt(i));
						//parcours le tables pas encore parcourues
						for(int j=0;j<tablesVector.size();j++){
							//si la table n'a pas encore été parcourue
							if(tablesVector.elementAt(j).getTableResource().equals(tableEtrangere)){
								double lengthOfTheTable=tablesVector.elementAt(j).getNodeWeight();
								//vérifie que la table n'a pas déjà été parcourue avec un chemin plus court
								if(lengthOfTheTable==-1||lengthOfTheTable>lengthOfThePath){
									if(!alreadyInPathVector(tableEtrangere)){
									path.add(new PathNode(table,foreignKeys.elementAt(i), lengthOfThePath, PathNode.ISAFOREIGNKEYCOLUMN));
									Vector<Statement> infosFK=parcours.infosSurUneColonne(foreignKeys.elementAt(i));
									for(int k=0;k<infosFK.size();k++){
										if(infosFK.elementAt(k).getPredicate().getLocalName().equals("belongsToClassMap")){
											double localPathLength=-1;
											if(isHeritageRelation(tableEtrangere,table)){
												localPathLength=lengthOfThePath+1;
												path.add(new PathNode(table,tableEtrangere,localPathLength,PathNode.ISATABLE,PathNode.ISHERITAGERELATION));
											}
											else if(isAssociationTableRelation(tableEtrangere)){
												double value=0.96;
												localPathLength=(lengthOfThePath+value)+(0.01*parcours.getArity(tableEtrangere));
												path.add(new PathNode(table,tableEtrangere,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONTABLE));
											}
											else{
												if(parcours.getArity(tableEtrangere)==-1){
													localPathLength=lengthOfThePath+1;
													boolean asswithoutatt=false;
													for(int l=0;l<awa.size();l++){
														if(awa.elementAt(l).getTable().equals(tableEtrangere)){
															asswithoutatt=true;
														}
													}
													if(asswithoutatt){
														path.add(new PathNode(table,tableEtrangere,(localPathLength-0.02),PathNode.ISATABLE,PathNode.ISANASSOCIATIONTABLEWITHOUTATTRIBUTS));
														localPathLength=localPathLength-0.02;
													}
													else{
														path.add(new PathNode(table,tableEtrangere,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONRELATION));
													}
												}
												else{
//													System.out.println(tableEtrangere);
													localPathLength=lengthOfThePath+1+(0.01*parcours.getArity(tableEtrangere));
													path.add(new PathNode(table,tableEtrangere,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONRELATION));
												}
											}
											tablesVector=editTableWeight(tablesVector, tableEtrangere, localPathLength);
										}
										
									}
									
								}
								}
							}
						}
					}
					findSomething=true;
				}
				//ajoute les clés étrangères inverses au chemin. Une clé étrangère inverse est une relation de clé étrangère mais dans le sens inverse
				if(findInverseForeignKeys(smallestPathTableStatements,primaryKey,tablesVector)){
					findSomething=true;
				}
				}
			}
			if(table==null){
				tablesVector.clear();
			}
			else if(table.equals(outputTable)){
				if(previousnodeIsOutput==true){
					tablesVector.clear();
				}
				else{
					tablesVector=editTableWeight(tablesVector, table, -1);
					tailleTablesVectorPrec--;
				}
				previousnodeIsOutput=true;
				
				
			}
			if(findSomething==false){
				tablesVector.clear();
			}
		}
		if(trouve){
			return path;
		}
		else{
			return null;
		}
	}
		
	/** find all attributs of a primary key for a selected table
	 * 
	 * @param stmts contain all informations about a table
	 * @return Vector<Resource> contain all attributs of the primary key
	 */
	private Vector<Resource> findPrimaryKey(Vector<Statement> stmts){
		Vector<Resource> primaryKey=new Vector<Resource>();
		String pk="";
		String namespace="";
//		String table=null;
		for(int i=0;i<stmts.size();i++){
			if(stmts.elementAt(i).getPredicate().getLocalName().equals("uriPattern")){
				pk=stmts.elementAt(i).getObject().toString();
				namespace=stmts.elementAt(i).getSubject().getNameSpace();
//				table=stmts.elementAt(i).getSubject().getLocalName();
				i=stmts.size();
			}
		}
		String[]split=pk.split("@@");
		for (int i=0;i<split.length;i++){
			if(i%2==1){
				pk=split[i].replace(".", "_");
				pk=namespace+pk;
				primaryKey.add(parcours.getModel().getResource(pk));
			}
		}
		return primaryKey;
	}
		
	
	private Vector<Table> deleteTheTable(Vector<Table> tables,Resource table){
		Vector<Table> tablesReturn=new Vector<Table>();
		for (int i=0;i<tables.size();i++){
			if(!tables.elementAt(i).getTableResource().equals(table)){
				tablesReturn.add(tables.elementAt(i));
			}
		}
		return tablesReturn;
	}

	/** edit weight between input table and the current table
	 * 
	 * @param tableVector
	 * @param table
	 * @param length
	 * @return
	 */
	private Vector<Table> editTableWeight(Vector<Table> tableVector,Resource table, double length){
		for(int i=0;i<tableVector.size();i++){
			Table tableObject=tableVector.elementAt(i);
			if(tableObject.getTableResource().equals(table)){
				if(tableVector.elementAt(i).getNodeWeight()==-1||tableVector.elementAt(i).getNodeWeight()>length){
					tableVector.elementAt(i).setTableWeight(length);
				}
			}
		}
		return tableVector;
	}
	
	/** find inverse foreigh keys of a table. An inverse foreigh key is foreigh key oriented in the over side.
	 * a foreigh key detection imply to incremente path weight for the linked table
	 * @param smallestPathTableStatements
	 * @param primaryKeys
	 * @param tableVector
	 * @return
	 */
	private boolean findInverseForeignKeys(Vector<Statement> smallestPathTableStatements,Vector<Resource> primaryKeys,Vector<Table> tableVector){
		boolean inverseForeignKeys=false;
		for (int i=0;i<smallestPathTableStatements.size();i++){
			if(smallestPathTableStatements.elementAt(i).getPredicate().getLocalName().equals("belongsToClassMap")){
				Resource column=smallestPathTableStatements.elementAt(i).getSubject();
				Resource tableResource=(Resource)smallestPathTableStatements.elementAt(i).getObject();
				String tableName=tableResource.getLocalName();
				Vector<Statement> columnInfo=parcours.infosSurUneColonne(column);
				Resource table=null;
				boolean alias=false;
				int joinNumber=0;
				//initialise la resource table
				for(int j=0;j<columnInfo.size();j++){
					//récupère la table de destination de la clé étrangère
					if(columnInfo.elementAt(j).getPredicate().getLocalName().equals("refersToClassMap")){
						table=(Resource)columnInfo.elementAt(j).getObject();
//						tableVector=editTableWeight(tableVector, table, lengthOfThePath);
					}
					//regarde si la colonne correspond à un alias
					else if(columnInfo.elementAt(j).getPredicate().getLocalName().equals("alias")){
						j=columnInfo.size();
						alias=true;
					}
					else if(columnInfo.elementAt(j).getPredicate().getLocalName().equals("join")){
						joinNumber++;
					}
				}
				for(int j=0;j<columnInfo.size();j++){
					if(columnInfo.elementAt(j).getPredicate().getLocalName().equals("join")&&alias==false&&joinNumber==1){
						String ns=column.getNameSpace();
						String columnName=column.getLocalName();
						columnName=columnName.substring(0, tableName.length())+"."+columnName.substring(tableName.length()+1);
						String localName=parcours.getForeignKeys().get(columnName);
						if(!(localName==null)){
						if(localName.contains("_")){
						localName=localName.replace(".", "_");
						for (int k=0;k<tableVector.size();k++){
							Resource tableVectorResource=tableVector.elementAt(k).getTableResource();
							if(tableVectorResource.equals(table)){
								double localPathLength=-1;
								path.add(new PathNode(tableResource,column,lengthOfThePath,PathNode.ISASIMPLECOLUMN));
								path.add(new PathNode(tableResource,parcours.getModel().getResource(ns+localName),lengthOfThePath,PathNode.ISANINVERSEFOREIGNKEYCOLUMN));
								
								if(isHeritageRelation(table,tableResource)){
									localPathLength=lengthOfThePath+1;
									path.add(new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISHERITAGERELATION));
//									PathNode bah=new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISHERITAGERELATION);
//									System.out.println("heritage relationship: "+bah.toString());
								}
								else if(isAssociationTableRelation(table)){
//									System.out.println(table);
									localPathLength=lengthOfThePath+.96+(0.01*parcours.getArity(table));
									path.add(new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONTABLE));
//									PathNode bah=new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONTABLE);
//									System.out.println("association table: "+bah.toString());
								}
								else{
									if(parcours.getArity(table)==-1){
										localPathLength=lengthOfThePath+1;
//										System.out.println(table);
										path.add(new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONRELATION));
//										PathNode bah=new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONRELATION);
//										System.out.println("simple association: "+bah.toString());
									}
									else{
//										System.out.println(table);
										localPathLength=lengthOfThePath+1+(0.01*parcours.getArity(table));
										path.add(new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONRELATION));
//										PathNode bah=new PathNode(tableResource,table,localPathLength,PathNode.ISATABLE,PathNode.ISASSOCIATIONRELATION);
//										System.out.println("association not binary: "+bah.toString());
									}
								}
								tableVector=editTableWeight(tableVector, table, localPathLength);
								parcours.getArity(table);
								inverseForeignKeys=true;
							}
						}}}
					}
				}
			}
		}
		return inverseForeignKeys;
	}
	
	/**search if a relation between 2 tables is an inheritance relation
	 * 
	 * @param inputTable
	 * @param outputTable
	 * @return boolean
	 */
	private boolean isHeritageRelation(Resource inputTable,Resource outputTable){
		boolean heritagerelation=false;
//		Vector<Statement> tables=new Vector<Statement>();
		StmtIterator stmts=model.listStatements();
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getSubject().equals(inputTable)){
				if(stmt.getPredicate().getLocalName().equals("subClassOf")){
					if(stmt.getObject().equals(outputTable)){
//						System.out.println(stmt);
						heritagerelation=true;
					}
				}
			}
		}
		return heritagerelation;
	}
	
	/**search if a table has inheritance relation
	 * 
	 * @param table
	 * @return boolean
	 */
	private boolean isHeritageRelation(Resource table){
		boolean heritagerelation=false;
//		Vector<Statement> tables=new Vector<Statement>();
		StmtIterator stmts=model.listStatements();
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getSubject().getLocalName().equals(table.getLocalName())){
				if(stmt.getPredicate().getLocalName().equals("subClassOf")){
//					System.out.println(stmt);
					heritagerelation=true;
				}
			}
		}
		return heritagerelation;
	}
	
	/**search if a table is an assocation table
	 * 
	 * @param table
	 * @return boolean
	 */
	private boolean isAssociationTableRelation(Resource table){
		boolean associationRelation=false;
		StmtIterator stmts=parcours.getModel().listStatements();
//		System.out.println(table);
		while(stmts.hasNext()&&(!associationRelation==true)){
			Statement stmt=stmts.next();
			if(stmt.getPredicate().getLocalName().equals("AssociatedTo")){
//				System.out.println(stmt);
				String assRelTable=stmt.getSubject().getLocalName();
//				Resource assRelProp=(Resource)stmt.getObject();
//				associationRelation=true;
				if(assRelTable.equals(table.getLocalName())){//||assRelProp.getLocalName().equals(table.getLocalName())){
//					System.out.println(stmt);
					associationRelation=true;
				}
			}
		}
		return associationRelation;
	}
		
	/**retrieve all foreign keys for a selected table
	 * 
	 * @param currentTable
	 * @return
	 */
	private Vector<Resource> findForeignKeys(Resource currentTable){
	Vector<Resource> foreignkeys=new Vector<Resource>();
	StmtIterator stmts=model.listStatements();
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getObject().isResource()){
				Resource object=(Resource)stmt.getObject();
				if(object.equals(currentTable)){
					Resource predicate=stmt.getPredicate();
					if(predicate.getLocalName().equals("refersToClassMap")){
						Resource foreignKey=stmt.getSubject();
						foreignkeys.add(foreignKey);
					}
				}
			}
		}
		return foreignkeys;
	}
	
	/**retrieve semantic annotation associated to a column
	 * 
	 * @param inputConcept
	 * @return
	 */
	private Resource findAnnotation(Resource inputConcept){
		Resource input=null;
		Vector<Statement> inputInfos=parcours.infosSurUneColonne(inputConcept);
		for (int i=0;i<inputInfos.size();i++){
			if(inputInfos.elementAt(i).getPredicate().getLocalName().equals("property")){
				input=(Resource)inputInfos.elementAt(i).getObject();
			}
		}
		return input;
	}
	
	/** find the table with the smallest path
	 * 
	 * @param tableVector
	 * @return
	 */
	private Vector<Statement> tableWithSmallestPath(Vector<Table>tableVector){
		Vector<Statement> tableInformations=new Vector<Statement>();
		int element=-1;
		double weight=-1;
		for (int i=0;i<tableVector.size();i++){
			double currentTableWeight=tableVector.elementAt(i).getNodeWeight();
			//cherche dans un premier temps toute table ayant un poids positif
			if(element<0){
				if(currentTableWeight>=0){
					element=i;
					weight=currentTableWeight;
				}
			}
			//conserve le poids positif le plus petit
			else if(currentTableWeight>=0&&weight>currentTableWeight){
				element=i;
				weight=currentTableWeight;
			}
		}
		//récupère les informations de la table trouvée précédemment
		if (!(element==-1||weight==-1)){
			Resource tableWithSmallestPath=tableVector.elementAt(element).getTableResource();
			tableInformations=parcours.infosSurUneTable(tableWithSmallestPath);
			lengthOfThePath=weight;
		}
		return  tableInformations;
	}
	
	/**search if a table has already been parsed
	 * 
	 * @param table
	 * @return
	 */
	private boolean alreadyInPathVector(Resource table){
		boolean alreadyInPath=false;
		boolean needToDelete=false;
		for (int i=0;i<path.size();i++){
			PathNode currentNode=path.elementAt(i);
//			System.out.println(currentNode);
			if(!(currentNode.getObjectNode()==null)){
			if(currentNode.getObjectNode().equals(table)){
				alreadyInPath=true;
				if(isHeritageRelation(table)){
					if(currentNode.getWeight()<lengthOfThePath){
						needToDelete=true;
					}
				}
				else if(isAssociationTableRelation(table)){
					double weight=lengthOfThePath+0.96+(0.01*parcours.getArity(table));
					if(currentNode.getWeight()<weight){
						needToDelete=true;
					}
				}
				else{
					if(parcours.getArity(table)==-1){
						double weight=lengthOfThePath+1;
						if(currentNode.getWeight()<weight){
							needToDelete=true;
						}
					}
					else{
						double weight=lengthOfThePath+1+(0.01*parcours.getArity(table));
						if(currentNode.getWeight()<weight){
							needToDelete=true;
						}
					}
				}
			}
		}
		}
		if(needToDelete==true){
			alreadyInPath=false;
		}
		
		return alreadyInPath;
	}
	
}
