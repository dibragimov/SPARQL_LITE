/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package researchproject;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import researchproject.Logging.RLogger;
import researchproject.Parser.OuterClassParser;
import researchproject.Parser.QueryParser;
import researchproject.Parser.RollUpParser;
import researchproject.Queries.GQueries;
import researchproject.Queries.LocalQueryBuilder;
import researchproject.SqlService.SqlRepClient;
import researchproject.SqlService.SqlService;
import researchproject.Virtuoso.VirtuosoClient;
import researchproject.mapping.JsonObjectParser;

/**
 *
 * MAIN CLASS
 */
public class ResearchProject {

    private static int[] queryNo = new int[]{0,1,2,3,4,5,6,7};    
    private static String americaDataset = "http://localhost:8890/DatasetAmerica";
    private static String europeDataset = "http://localhost:8890/DatasetEurope";
    private static String asiaDataset = "http://localhost:8890/DatasetAsia";
    
    public static void main(String[] args) {
       RLogger.initialize();       
       SqlRepClient.Initialize();
      for(int i =0; i < queryNo.length; i++)
      {
        
            int queryNo = i ;//i+1;
            String globalQuery = GQueries.getQuery(queryNo);
            RLogger.info("|| GLOBAL QUERY || --> " + globalQuery);
            System.out.println("globalQuery --> " + globalQuery);
            String localizedQuery = LocalQueryBuilder.build(globalQuery);
            RLogger.info("|| LOCALIZED QUERY || --> " + localizedQuery);
            System.out.println("localizedQuery --> " + localizedQuery);
            String entailedQuery = localizedQuery;//Helper.RemoveDoubleSpaces(EntailmentParser.GetBasicQuery(localizedQuery));

            exectureSubQuery(entailedQuery, (queryNo+1));
       }
     }
    
    public static void exectureSubQuery(String queryString, int index)
    {
        System.out.println(queryString);
        System.out.println("=========");
        Map<String, List<String>> tableDetails =new HashMap<String, List<String>>();
        List<String> resultSet = QueryParser.GetInnerQuery(queryString);
        int tableIndex = 1;
        if(resultSet.isEmpty())
        {
                System.out.println("=== " + queryString);
                try {                 
                    
                    System.out.println("query -->"+ queryString);
                    String table = "Table_Query" + index + "_table" + tableIndex;
                    
                    String europeQuery = RollUpParser.GetFilteredCountries(queryString, "6255148");
                    JSONObject jObject = VirtuosoClient.sendRequest(europeQuery, europeDataset);
                    List<String> columns = SqlService.createTable(jObject, table);
                    String sql = JsonObjectParser.populateTable(table, jObject);
                    SqlRepClient.runStatement(sql);  
                    
                    String americaQuery = RollUpParser.GetFilteredCountries(queryString, "6255149");
                    jObject = VirtuosoClient.sendRequest(americaQuery, americaDataset);
                    if(columns.isEmpty())     
                        columns = SqlService.createTable(jObject, table);
                    
                    sql = JsonObjectParser.populateTable(table, jObject);                    
                    SqlRepClient.runStatement(sql);
                    
                    String asiaQuery = RollUpParser.GetFilteredCountries(queryString, "6255147");
                    jObject = VirtuosoClient.sendRequest(asiaQuery, asiaDataset);
                    if(columns.isEmpty())            
                        columns = SqlService.createTable(jObject, table);
                    
                    sql = JsonObjectParser.populateTable(table, jObject);
                    SqlRepClient.runStatement(sql);
                    String columnsComma = "";
                    boolean isFirst = true;
                    for(String column : columns)
                    {
                        if(isFirst) 
                        {
                            columnsComma += column +" ";
                            isFirst = false;
                        }
                        else                             
                            columnsComma +=", " + column +" ";
                    }
                    SqlRepClient.selectAll("SELECT "+columnsComma+" FROM " + table);
                }
                catch (Exception e)
                {
                    System.out.println("corrupted query occured: " + e);
                }
        }
        else 
        {
            for(String query : resultSet)
            {
                //System.out.println(query);
                System.out.println("=== " + query);
                try {                 
                    String table = "Table_Query" + index + "_table" + tableIndex;
                    System.out.println("query -->"+ query);
                    
                    String americaQuery = RollUpParser.GetFilteredCountries(query, "6255149");
                    JSONObject jObject = VirtuosoClient.sendRequest(americaQuery, americaDataset);
                    List<String> columns = SqlService.createTable(jObject, table);
                    String sql = JsonObjectParser.populateTable(table, jObject);
                    SqlRepClient.runStatement(sql);                    
                    String euQuery = RollUpParser.GetFilteredCountries(query, "6255148");
                    jObject = VirtuosoClient.sendRequest(euQuery, europeDataset);
                    if(columns.isEmpty())            
                        columns = SqlService.createTable(jObject, table);
                    
                    sql = JsonObjectParser.populateTable(table, jObject);                    
                    SqlRepClient.runStatement(sql);
                    String asiaQuery = RollUpParser.GetFilteredCountries(query, "6255147");
                    jObject = VirtuosoClient.sendRequest(asiaQuery, asiaDataset);
                    if(columns.isEmpty())            
                        columns = SqlService.createTable(jObject, table);
                    sql = JsonObjectParser.populateTable(table, jObject);
                    SqlRepClient.runStatement(sql);
                    
                    tableDetails.put(table, columns);                 
                }
                catch (Exception e)
                {
                    System.out.println("corrupted query occured: " + e);
                }
                tableIndex += 1;
            }
            String outerSqlstat = OuterClassParser.buildOuterQuery(queryString, tableDetails, index); 
            outerSqlstat = outerSqlstat.replaceAll(" [a-zA-z0-9] ", " ");
            System.out.println(outerSqlstat);
            SqlRepClient.selectAll(outerSqlstat);
        }
    }

}
