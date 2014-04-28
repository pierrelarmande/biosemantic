package com.wscreation.main;

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.wscreation.createrequest.CreateRequest;
import com.wscreation.findpath.FindOnePath;
import com.wscreation.findpath.PathNode;

public class FindOnePathMain {
	public static void main(String[] args) {
		//mapping file
		String mappingPath="mapping-MEDOC-TROPGENE_COTTON.n3";
//		String mappingPath="mapping-MEDOC-GRAMENE_MARKER.n3";
//		String mappingPath="mapping-GRAMENEDB-ONTOLOGY34.n3";
//		String mappingPath="mapping-ODB_MEDOC_3.n3";
//		FindOnePath path= new FindOnePath("mapping-TG_LOCAL_RICE.N3");
//		FindOnePath path= new FindOnePath("mapping_test.N3");
		FindOnePath path= new FindOnePath(mappingPath);
//		FindOnePath path= new FindOnePath("mapping_association_modifie.N3");
		//input concept
		Resource inputR=path.getModel().getResource("http://gcpdomainmodel.org/GCPDM#GCP_Study");
//		Resource inputR=path.getModel().getResource("http://edamontology.org/data#identifier");
//		Resource inputR=path.getModel().getResource("http://gcpdomainmodel.org/GCPDM#input");
//		Resource outputR=path.getModel().getResource("jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#reference_name");
		
		//output concept
		Resource outputR=path.getModel().getResource("http://gcpdomainmodel.org/GCPDM#GCP_GenomicFeatureDetector");//enomicFeatureDetector");
//		Resource outputR=path.getModel().getResource("http://edamontology.org/data#sequence_position");
//		Resource outputR=path.getModel().getResource("http://gcpdomainmodel.org/GCPDM#output");
//		Resource inputR=path.getModel().getResource("jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#author_name");
//		Resource res3=path.parcours.getModelToParse().getResource("file:///c:/mapping.n3#markergenotypingstudy");//_id_marker");
		
		//recherche tous les elements du schéma annotés avec le concept d'entrée
		Vector<Statement> stmts=path.parcours.retrieveColumnWithThisAnnotation(inputR);
		//recherche tous les elements du schéma annotés avec le concept de sortie
		Vector<Statement> stmts2=path.parcours.retrieveColumnWithThisAnnotation(outputR);
//		System.out.println(stmts.elementAt(0));
//		System.out.println(stmts2.elementAt(0));
//		for(int i=0;i<stmts2.size();i++){
////			System.out.println(stmts2.elementAt(i));
//		}
		if(stmts.size()>0&&stmts2.size()>0){
			if(stmts.size()==1&&stmts2.size()==1){
				Resource input=stmts.elementAt(0).getSubject();
				path.setInputColumn(input);
//				System.out.println(input);
				Resource output=stmts2.elementAt(0).getSubject();
				path.setOutputColumn(output);
//				System.out.println(output);
				Vector<PathNode> pathTree=path.findOnePath(false,null);
				
//				pathTree.add(0, null);
				for(int i=0;i<pathTree.size();i++){
					System.out.println(pathTree.elementAt(i));
				}
				//création de la requête
				CreateRequest cr=new CreateRequest(mappingPath,pathTree, input, output,inputR,outputR,path);
				Vector<String> stn=cr.getRequestPath();
				for(int i=0;i<stn.size();i++){
					System.out.println(stn.elementAt(i));
				}
//					PathNode node=pathTree.elementAt(i);
////					if(node.getNodeType()==PathNode.ISATABLE){
////						if(node.getSubjectNode()==null){
//					System.out.println(pathTree.elementAt(i).getSubjectNode()+"\t"+pathTree.elementAt(i).getObjectNode()+"\t"+pathTree.elementAt(i).getWeight()+"\t"+pathTree.elementAt(i).getNodeType()+"\t"+pathTree.elementAt(i).getRelationType());
////							System.out.println(node.getObjectNode().getLocalName());
////						}
////					}
//				}
			}
		}
	}
}
