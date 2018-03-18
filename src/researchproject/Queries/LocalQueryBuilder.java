/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package researchproject.Queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import researchproject.Logging.RLogger;
import researchproject.Sparql.SparqlService;
import researchproject.mapping.GlobalSchemaParser;
import researchproject.mapping.TripleCombinator;
import researchproject.models.Triple;

/**
 *
 * THIS CLASS RESPONSIBLE FOR LOCALIZING GLOBAL QUERIES
 */
public class LocalQueryBuilder {
    public final static String _datasetUri = "http://localhost:8890/Data-fixed22" ;
    
    public static String build(String globalQuery)
    {
       List<String> subqueries = GlobalSchemaParser.getSchema(globalQuery);
       List<String> subQueryBlocks = GlobalSchemaParser.getSchemaTriples(globalQuery);
       List<String> variablesQueries = GlobalSchemaParser.getVariablesQuery(globalQuery);
       Map<String,String> variables = new HashMap<>();
       for(int j=0; subqueries.size() > j; j++)
       {
           try 
           {
           //obtaining variables from Triples to store and later replace them (global with local)
            
            String variablesQuery = variablesQueries.get(j);            
            RLogger.info("|| SELECT SUBQUERY FOR VARIABLES || --> " +  variablesQuery);
            
            //JSONObject subQueryVariables = VirtuosoClient.sendRequest(variablesQuery, _globalSchemaUri);
            //variables.putAll(JsonObjectParser.getVariables(subQueryVariables));
            List<String> subQueryVariables2 = SparqlService.getVariables(variablesQuery);
            Map<String,String> pairs = GlobalSchemaParser.GetVariables(subQueryVariables2);
            variables.putAll(pairs);
            //obtaining triples from localSchema
            String subQuery =subqueries.get(j);
            RLogger.info("|| CONSTRUCT SUBQUERY FOR TRIPLES || --> " +  subQuery);
            List<Triple> contstructedTriples =  SparqlService.getCnsctFromGlobalMapping(subQuery);
            
            // checking all combinations of triples like (1st,3th,5th triples)(1st,2nd,4th,5th triples)
            // to find local Schema triples match
            String[][] combinations =  TripleCombinator.getAllCombinations(contstructedTriples.size());
            Set<String> localSchemas = new HashSet<>();
            localSchemas.addAll(getLocalSchemaMatches(combinations,contstructedTriples));

            List<Triple> result = getTriplesFromLsmt(localSchemas);
            
            String globalTriples = subQueryBlocks.get(j);            
            String localTriples = TripleCombinator.buildlocalizedRequest(result);
            //replacing existing global schema with local one 
            globalQuery = globalQuery.replace(globalTriples," "+localTriples+" ");
           }
           catch(Exception ex) {
                RLogger.info("||EXCEPTION WHILE BUILDING LOCAL QUERY || --> " + ex.getMessage());
           }
       }
       //replace variables in SELECT params 
       for (Map.Entry<String, String> entry : variables.entrySet())
       {
           RLogger.info("|| VARIABLE'S PAIR || --> " + entry.getKey()+  " == "+ entry.getValue());
           globalQuery = globalQuery.replaceAll("\\?"+entry.getKey()+"(?![A-Za-z]+)",entry.getValue());
       }
       
       return globalQuery;// localizedSubQueries;
    }
    
    // gettings subject predicate object from Local Schema Matched Triples 
    private static List<Triple> getTriplesFromLsmt( Set<String> localSchemas)
    {
            List<Triple> result = new ArrayList<>();
            for(String schema : localSchemas) {
                 String lSchema = TripleCombinator.BuildLSchemaRequest(schema);
                 RLogger.info("||  QUERY FOR LSMT TRIPLE PATTERN || --> " +  lSchema);
                 List<String> pairs = SparqlService.getFromGlobalMapping(lSchema);
                 List<Triple> triples = GlobalSchemaParser.getTriples(pairs);   
                 result.addAll(triples);
            } 
            return result;
    }
    // Getting lsmt by sending localized queries
    private static List<String> getLocalSchemaMatches(String[][] combinations,List<Triple> contstructedTriples)
    {
            
            List<String> lSchema =  new ArrayList<>();
            for(int i = combinations.length-1; i >= 0; i--)
            {
                List<Triple> triples = TripleCombinator.getTriples(combinations[i],contstructedTriples);
                String query = TripleCombinator.localizedQuery(triples);
                List<String> pairs = SparqlService.getFromGlobalMapping(query);
                List<String> lsmts = GlobalSchemaParser.getLsmt(pairs); 
                if(lsmts.size() >0)
                {                    
                    RLogger.info("|| LSMT QUERY || --> " +  query);
                    for(String lsmt : lsmts)
                        RLogger.info("|| RETURNED LSMT || --> " +  lsmt);
                }
                //System.out.println("=======" + lsmts +"=======");   
                lSchema.addAll(lsmts);
                //System.out.println("=======" + i +"=======");
            }
            return lSchema;
    }
}
