package com.wscreation.main;

import java.io.File;
import java.util.Vector;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

public class Requete_SPARQL {
	public static void main(String[] args) {
		
		Model m = new ModelD2RQ("mapping-TG_LOMAGNE_RICE_final.n3");
		Long time1=System.currentTimeMillis();
//		String sparql="PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>SELECT DISTINCT ?marker_name WHERE {?study_id_study gcpdm:GCP_Study ?study_name.FILTER regex(?study_name,\"^HAPLORYZA_V2_TOP$\").?marker_id_marker vocab:markerstudy ?study_id_study.?marker_id_marker gcpdm:GCP_GenomicFeatureDetector ?marker_name.}";
		/**1456**/
//		String sparql=new StringBuilder(""+
//				"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
//				"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
//				"SELECT distinct ?marker_name ?study_name  WHERE {"+
//				  "?id_study gcpdm:GCP_Study ?study_name."+
//				  "?id_gs vocab:genotypingstudy_id_study ?id_study."+
//				  "?id_mgs_key vocab:markergenotypingstudy_id_genotypingstudy ?id_gs."+
//				  "?id_mgs_key vocab:markergenotypingstudy_id_marker ?id_marker."+
//				  "?id_marker gcpdm:GCP_GenomicFeatureDetector ?marker_name."+
//				  "FILTER regex(?study_name,\"^.*$\")"+
//				"}").toString();
		/**860**/
//		String sparql=new StringBuilder(""+
//				"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
//				"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
//				"SELECT distinct ?dnasample_name ?study_name  WHERE {"+
//				  "?id_study gcpdm:GCP_Study ?study_name."+
//				  "?id_gs vocab:genotypingstudy_id_study ?id_study."+
//				  "?id_dgs_key vocab:dnasamplegenotypingstudy_id_genotypingstudy ?id_gs."+
//				  "?id_dgs_key vocab:dnasamplegenotypingstudy_id_dnasample ?id_dnasample."+
//				  "?id_dnasample gcpdm:GCP_SimpleIdentifier ?dnasample_name."+
//				  "FILTER regex(?study_name,\"^.*$\")"+
//				"}").toString();
		/**12302**/
//		String sparql=new StringBuilder(""+
//				"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
//				"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
//				"SELECT DISTINCT ?study_name ?marker_name WHERE {"+
//					"FILTER regex(?study_name,\"^.*$\")."+
//					"?study_id_study gcpdm:GCP_Study ?study_name."+
//					"?marker_id_marker vocab:markerstudy ?study_id_study."+
//					"?marker_id_marker gcpdm:GCP_GenomicFeatureDetector ?marker_name."+
//					"}").toString();
		/**2055**/
		String sparql=new StringBuilder(""+
			"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
			"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
			"SELECT DISTINCT ?dnasample_name ?germplasm_name  WHERE {"+
				"FILTER regex(?dnasample_name,\"^.*$\")."+
				"?dnasample_id_dnasample gcpdm:GCP_SimpleIdentifier ?dnasample_name."+
				"?dnasample_id_dnasample vocab:dnasample_id_germplasm ?dnasample_id_germplasm."+
				"?dnasample_id_germplasm gcpdm:GCP_Germplasm ?germplasm_name."+
				"}").toString();
		Query q1 = QueryFactory.create(sparql); 
		ResultSet rs1 = QueryExecutionFactory.create(q1, m).execSelect();
		
		Vector<String> dnaSamplesNames=new Vector<String>();
		while (rs1.hasNext()) {
		    QuerySolution row = rs1.nextSolution();
//		    dnaSamplesNames.add(row.getLiteral("marker_name").getString());
		}
		System.out.println("number :"+rs1.getRowNumber());

//		File file=new File("mapping-TG_LOMAGNE_RICE_final.n3");
//		System.out.println(file.getAbsolutePath());
//	ModelD2RQ m = new ModelD2RQ(file.getPath());
//	String sparql1 = 
//		"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>" +
//		"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
//		"SELECT ?study_name ?genotypingstudy_id WHERE {"+
//		"	  ?study_id gcpdm:GCP_Study ?study_name."+
//		"	  ?genotypingstudy_id vocab:genotypingstudy_id_study ?study_id."+
//		"}";
//	Query q1 = QueryFactory.create(sparql1); 
//	ResultSet rs1 = QueryExecutionFactory.create(q1, m).execSelect();
//	Vector<String> genotypeStudies=new Vector<String>();
//	while (rs1.hasNext()) {
//	    QuerySolution row = rs1.nextSolution();
//	    genotypeStudies.add(row.getLiteral("study_name").getString());
//	    System.out.println(row.getLiteral("study_name").getString());
//	}
//	String sparql1 = 
//		"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
//		"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
//		"SELECT DISTINCT ?study_id ?study_name ?germplasm_name WHERE {"+
//		"?study_id gcpdm:GCP_Study ?study_name."+
//		"FILTER regex(?study_name,\"^.*$\")."+
//		"?genotypestudy_id vocab:genotypingstudy_id_study ?study_id."+
//		"?key vocab:dnasamplegenotypingstudy_id_genotypingstudy ?genotypestudy_id."+
//		"?key vocab:dnasamplegenotypingstudy_id_dnasample ?dnasample."+
//		"?dnasample vocab:dnasample_id_germplasm ?germplasm_id."+
//		"?germplasm_id gcpdm:GCP_Germplasm ?germplasm_name."+
//		"}";
//	ResultSet rs1 = QueryExecutionFactory.create(sparql1, m).execSelect();
//	Vector<String> dnaSamplesNames=new Vector<String>();
//	int i=0;
	
//	while (rs1.hasNext()) {
//		i++;
//	    QuerySolution row = rs1.nextSolution();
//	    System.out.println("<gcpdm:GCP_Germplasm/>"+row.getLiteral("germplasm_name").getString()+"<gcpdm:GCP_SimpleIdentifier/>"+row.getLiteral("study_name").getString());
//	}
//	System.out.println(rs1.getRowNumber());
	Long time2=System.currentTimeMillis();
	System.out.println(time2-time1);
	}
}
