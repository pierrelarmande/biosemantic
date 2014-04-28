package com.wscreation.test;

import java.io.File;
import java.util.Vector;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

	public class TestSPARQLRequest {
		public static void main(String[] args) {
			String file="mapping-LOMAGNE-TROPGENE_RICE.n3";
//			String file="mapping_entier.n3";
//			System.out.println(file.getAbsolutePath());
			ModelD2RQ m = new ModelD2RQ(file);
			String study_name="HAPLORYZA";
			String sparql1 = 
				"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
				"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
				"SELECT DISTINCT ?study_id ?study_name ?germplasm_name WHERE {"+
				"?study_id gcpdm:GCP_Study ?study_name."+
				"FILTER regex(?study_name,\"^"+study_name+"$\")."+
				"?genotypestudy_id vocab:genotypingstudy_id_study ?study_id."+
				"?key vocab:dnasamplegenotypingstudy_id_genotypingstudy ?genotypestudy_id."+
				"?key vocab:dnasamplegenotypingstudy_id_dnasample ?dnasample."+
				"?dnasample vocab:dnasample_id_germplasm ?germplasm_id."+
				"?germplasm_id gcpdm:GCP_Germplasm ?germplasm_name."+
				"}";
			String sparql2 =
				"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
				"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
				"SELECT DISTINCT ?microsat_marker_id ?germplasm_name WHERE {"+
				"?microsat_id vocab:microsatellite_id_marker ?microsat_marker_id."+
				"FILTER regex(?microsat_marker_id,\"^"+study_name+"$\")."+
				"?genotypestudy_id vocab:genotypingstudy_id_study ?study_id."+
				"?key vocab:dnasamplegenotypingstudy_id_genotypingstudy ?genotypestudy_id."+
				"?key vocab:dnasamplegenotypingstudy_id_dnasample ?dnasample."+
				"?dnasample vocab:dnasample_id_germplasm ?germplasm_id."+
				"?germplasm_id gcpdm:GCP_Germplasm ?germplasm_name."+
				"}";
			String sparql3=
				"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
				"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
				"SELECT DISTINCT ?study_name ?marker_name WHERE {"+
				"FILTER regex(?study_name,\"^"+study_name+"$\")." +
				"?study_id_study gcpdm:GCP_Study ?study_name."+
				"?marker_id_marker vocab:markerstudy ?study_id_study."+
				"?marker_id_marker gcpdm:GCP_GenomicFeatureDetector ?marker_name."+
				"}";
			ResultSet rs1 = QueryExecutionFactory.create(sparql1, m).execSelect();
//			Vector<String> dnaSamplesNames=new Vector<String>();
			while (rs1.hasNext()) {
			    QuerySolution row = rs1.nextSolution();
System.out.println("<gcpdm:GCP_Germplasm/>"+row.getLiteral("marker_name").getString()+"<gcpdm:GCP_SimpleIdentifier/>"+row.getLiteral("study_name").getString());
			}	
//			String sparql1 = 
//				"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>" +
//				"PREFIX vocab: <jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE/vocab#>"+
//				"SELECT ?study_name ?genotypingstudy_id WHERE {"+
//				"	  ?study_id gcpdm:GCP_Study ?study_name."+
//				"	  ?genotypingstudy_id vocab:genotypingstudy_id_study ?study_id."+
//				"}";
//			Query q1 = QueryFactory.create(sparql1); 
//			ResultSet rs1 = QueryExecutionFactory.create(q1, m).execSelect();
//			Vector<String> genotypeStudies=new Vector<String>();
//			System.out.println(rs1.getRowNumber());
//			while (rs1.hasNext()) {
//			    QuerySolution row = rs1.nextSolution();
//			    System.out.println(row.getLiteral("study_name"));
//			}
		}
	}
