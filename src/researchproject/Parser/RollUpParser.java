/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package researchproject.Parser;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import researchproject.Virtuoso.VirtuosoClient;
import researchproject.mapping.JsonObjectParser;

/**
 *
 * @author Iskandar
 */
public class RollUpParser {
    public static String GetServiceCountries(String query, String continent)
    {
        //SERVICE <http://lod2.openlinksw.com/sparql> { ?geoCountries <http://www.geonames.org/ontology#parentFeature> <http://sws.geonames.org/6255148/> . ?geoCountries a <http://www.geonames.org/ontology#Feature> . } Filter (?geoNames = ?geoCountries) }
        String continentService = " <https://www.w3.org/TR/2004/REC-owl-features-20040210/#sameAs> ?geoNames . SERVICE <http://lod2.openlinksw.com/sparql> { ?geoNames <http://www.geonames.org/ontology#parentFeature> <http://sws.geonames.org/"+continent+"/> . }";
        String regex = "\\?[a-zA-Z]+ rdf:type <http:\\/\\/www4\\.wiwiss\\.fu-berlin\\.de\\/bizer\\/bsbm\\/v01\\/vocabulary\\/Country> \\.\\}";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(query);
        String outerquery = null;
        int startIndex = 0;
        if(match.find()) {
            String triple = match.group(0);            
            String variable = triple.substring(1, triple.indexOf(" "));
            continentService = "?"+variable + continentService;
            System.out.println(" found match "+  match.group(0));
            int length = triple.length();
            startIndex = query.indexOf(triple,startIndex + length);
            query = query.substring(0,(startIndex+length)) + continentService +  query.substring((startIndex+length));
        }
        return query;  
    }   
     
    public static String GetFilteredCountries(String query, String continent)
    {
        String continentService = " <https://www.w3.org/TR/2004/REC-owl-features-20040210/#sameAs> ?geoNames . SERVICE <http://lod2.openlinksw.com/sparql> { ?geoNames <http://www.geonames.org/ontology#parentFeature> <http://sws.geonames.org/"+continent+"/> . }";
        String regex = "\\?[a-zA-Z]+ rdf:type <http:\\/\\/www4\\.wiwiss\\.fu-berlin\\.de\\/bizer\\/bsbm\\/v01\\/vocabulary\\/Country> \\.\\}";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(query);
        int startIndex = 0;
        while(match.find()) {
            String triple = match.group(0);            
            String variable = triple.substring(1, triple.indexOf(" "));
            continentService = "?"+variable + getFilterCountries(continent);
            //System.out.println(" found match "+  match.group(0));
            int length = triple.length();
            startIndex = query.indexOf(triple,startIndex + length);
            query = query.substring(0,(startIndex+length)) + continentService +  query.substring((startIndex+length));
        }
        return query;  
    }
    private static String getFilterCountries(String continent)
    {
        String statement = "Select * { SERVICE <http://lod2.openlinksw.com/sparql> { ?geoNames <http://www.geonames.org/ontology#parentFeature> <http://sws.geonames.org/"+continent+"/> . ?geoNames a <http://www.geonames.org/ontology#Feature> . } }";
        JSONObject jsonObject = VirtuosoClient.sendRequest(statement, "");
        List<String> geoNames =  JsonObjectParser.getGeoNames(jsonObject);
        String continentService = " <https://www.w3.org/TR/2004/REC-owl-features-20040210/#sameAs> ?geoNames . FILTER (";
        boolean isFirst = true;
        for(String geoName : geoNames)
        {
            if(isFirst){
                continentService += " ?geoNames = <"+geoName+">";
                isFirst = false;
            }
            else
                continentService += " || ?geoNames = <"+geoName+">";
        }
        continentService += ")";
        return continentService;
    }
}
