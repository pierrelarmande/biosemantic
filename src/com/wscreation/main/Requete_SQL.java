package com.wscreation.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Requete_SQL {
	
	public static void main(String[] args) {
		try{
		Long time1=System.currentTimeMillis();
		Class.forName("org.postgresql.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection(
		   "jdbc:postgresql://medoc.cirad.fr/gnpannot_musa","guest", "guest");
		connection.close();
//		try{
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//		}
//		catch(InstantiationException ex){System.out.println(ex.getMessage());}
//		catch(IllegalAccessException ex){System.out.println(ex.getMessage());}
//		catch(ClassNotFoundException ex){System.out.println(ex.getMessage());}

//		try{
////			String query="SELECT s.name, gs.id_genotypingstudy FROM study s, genotypingstudy gs WHERE s.id_study = gs.id_study";
////			String query="SELECT s.name, m.name FROM `marker` m, `study` s, markerstudy ms WHERE ms.id_study=s.id_study AND m.`id_marker`=ms.id_marker;";
////			String query="SELECT GS.id_study, M.name FROM marker M, study S, genotypingstudy GS, markergenotypingstudy MGS WHERE GS.id_study = S.id_study AND GS.id_genotypingstudy = MGS.id_genotypingstudy AND MGS.id_marker = M.id_marker";
////			String query="SELECT distinct s.id_study, g.name FROM study s, phenotypingstudy ps, germplasmphenotypingstudy gps, germplasm g WHERE s.id_study = ps.id_study AND gps.id_phenotypingstudy = ps.id_phenotypingstudy AND gps.id_germplasm = g.id_germplasm"; 
////			String query="SELECT DISTINCT s.id_study, g.name FROM study s, phenotypingstudy ps, germplasmphenotypingstudy gps, germplasm g WHERE s.id_study = ps.id_study AND gps.id_phenotypingstudy = ps.id_phenotypingstudy AND gps.id_germplasm = g.id_germplasm UNION SELECT DISTINCT s.id_study, g.name FROM study s, genotypingstudy gs, dnasamplegenotypingstudy dgs, dnasample ds, germplasm g WHERE s.id_study = gs.id_study AND dgs.id_genotypingstudy = gs.id_genotypingstudy AND dgs.id_dnasample = ds.id_dnasample AND ds.id_germplasm = g.id_germplasm UNION SELECT DISTINCT s.id_study, g.name FROM study s, varietalstudy vs, varietalsituation vst,  germplasm g WHERE s.id_study = vs.id_study AND vst.id_varietalstudy = vs.id_varietalstudy AND vst.id_germplasm = g.id_germplasm UNION SELECT DISTINCT s.id_study, g.name FROM study s, mapstudy ms, map m, mapfeature mf, marker mr, microsatellite mst, germplasmmicrosat gms, germplasm g WHERE s.id_study = ms.id_study AND m.id_mapstudy = ms.id_mapstudy AND mf.id_map = m.id_map AND mf.id_marker = mr.id_marker AND mst.id_marker = mr.id_marker AND gms.id_microsatellite = mst.id_microsatellite AND gms.id_germplasm = g.id_germplasm";
////			String query="SELECT DISTINCT s.id_study, g.name FROM study s, phenotypingstudy ps, germplasmphenotypingstudy gps, germplasm g WHERE s.id_study = ps.id_study AND gps.id_phenotypingstudy = ps.id_phenotypingstudy AND gps.id_germplasm = g.id_germplasm AND s.name REGEXP 'HAPLORYZA' UNION SELECT DISTINCT s.id_study, g.name FROM study s, genotypingstudy gs, dnasamplegenotypingstudy dgs, dnasample ds, germplasm g WHERE s.id_study = gs.id_study AND dgs.id_genotypingstudy = gs.id_genotypingstudy AND dgs.id_dnasample = ds.id_dnasample AND ds.id_germplasm = g.id_germplasm AND s.name REGEXP 'HAPLORYZA'UNION SELECT DISTINCT s.id_study, g.name FROM study s, varietalstudy vs, varietalsituation vst,  germplasm g WHERE s.id_study = vs.id_study AND vst.id_varietalstudy = vs.id_varietalstudy AND vst.id_germplasm = g.id_germplasm AND s.name REGEXP 'HAPLORYZA' UNION SELECT DISTINCT s.id_study, g.name FROM study s, mapstudy ms, map m, mapfeature mf, marker mr, microsatellite mst, germplasmmicrosat gms, germplasm g WHERE s.id_study = ms.id_study AND m.id_mapstudy = ms.id_mapstudy AND mf.id_map = m.id_map AND mf.id_marker = mr.id_marker AND mst.id_marker = mr.id_marker AND gms.id_microsatellite = mst.id_microsatellite AND gms.id_germplasm = g.id_germplasm AND s.name REGEXP 'HAPLORYZA'";
//			String query="SELECT SQL_NO_CACHE DISTINCT D.id_dnasample, D.name FROM dnasample D INNER JOIN dnasamplegenotypingstudy DSGS ON DSGS.id_dnasample = D.id_dnasample INNER JOIN genotypingstudy GS ON GS.id_genotypingstudy = DSGS.id_genotypingstudy INNER JOIN study S ON GS.id_study = S.id_study WHERE S.name = \"HAPLORYZA_V2_TOP\"";
////			String query="SELECT SQL_NO_CACHE DISTINCT M.id_marker, M.name FROM marker M inner join markergenotypingstudy MGS on MGS.id_marker=M.id_marker inner join genotypingstudy GS on GS.id_genotypingstudy=MGS.id_genotypingstudy inner join study S on GS.id_study = S.id_study WHERE S.name REGEXP '.*'";
//			Connection connection=DriverManager.getConnection("jdbc:mysql://lomagne.cirad.fr/TROPGENE_RICE", "****", "****");
//			Statement statement = connection.createStatement();
//			ResultSet rs = statement.executeQuery(query);
//			if (rs != null){
//				while(rs.next()){
//					System.out.println(rs.getString(1)+" "+rs.getString(2));
//				}
//			}
//			connection.close();
//			Long time2=System.currentTimeMillis();
//			System.out.println((time2-time1));
//			
		}
		catch(SQLException e){System.out.println(e.getMessage());}
		catch(ClassNotFoundException ex){System.out.println(ex.getMessage());}
	}

}
