/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package researchproject.Sparql;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.graph.Triple;
import org.apache.jena.util.FileManager;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import researchproject.Parser.QueryParser;





/**
 *
 * THIS CLASS IS RESPONSIBLE FOR EXECUTING SPARQL REQUESTS IN JENA
 */
public class SparqlService {
    private static Model _gInstance = null;
    private static final String _mappingPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"Library"+System.getProperty("file.separator")+"mapping-extended.ttl";
    private static final String _gSchemaPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"Library"+System.getProperty("file.separator")+"global-schema-extended.ttl";
   
    public static Model getGSchemaInstance()
    {
        if(_gInstance == null)
        {
        FileManager.get().addLocatorClassLoader(SparqlService.class.getClassLoader());
        _gInstance = FileManager.get().loadModel(_gSchemaPath);
        }
        return _gInstance;
    }
    
    private static Model _mInstance = null;
    public static Model getMappingInstance()
    {
        if(_mInstance == null)
        {
        FileManager.get().addLocatorClassLoader(SparqlService.class.getClassLoader());
       _mInstance = FileManager.get().loadModel(_mappingPath);
        }
        return _mInstance;
    }   
    public static List<String> sparqlTest(String queryString, int index) {
        
       // String queryString = QueryParser.getQuery();
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qExecutor = QueryExecutionFactory.create(query,SparqlService.getGSchemaInstance());
        List<String> result = new ArrayList<String>();
        try {
            ResultSet results =qExecutor.execSelect();
            while(results.hasNext()) {
                //QuerySolution soln = results.nextSolution();
                //Literal name = soln.getLiteral("productType");
                //System.out.println(name);
                result.add(results.next().toString());
               // System.out.println( results.next() );
            }
        }
        finally  {
            qExecutor.close();
        }
        return result;
    }
     public static List<String> getFromGlobalMapping(String queryString)
     {      
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qExecutor = QueryExecutionFactory.create(query,SparqlService.getMappingInstance());
        List<String> result = new ArrayList<String>();
        try {
            ResultSet results =qExecutor.execSelect();
            while(results.hasNext()) {
                //QuerySolution soln = results.nextSolution();
                //Literal name = soln.getLiteral("productType");
                //System.out.println(name);
                result.add(results.next().toString());
               // System.out.println( results.next() );
            }
        }
        finally  {
            qExecutor.close();
        }
        return result;
    }
    public static  List<researchproject.models.Triple> getCnsctFromGlobalMapping(String queryString)
    {      
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qExecutor = QueryExecutionFactory.create(query,SparqlService.getGSchemaInstance());
        List<researchproject.models.Triple> result = new ArrayList<>();
        try {
            Model model =qExecutor.execConstruct();
            StmtIterator cit = model.listStatements();
            while(cit.hasNext()) {
                 Statement stmt = cit.nextStatement();
                 Triple triple = stmt.asTriple();
               // System.out.print(" s: " +triple.getSubject()); System.out.print(" p: " +triple.getPredicate()); System.out.print(" o: " +triple.getObject());
               // System.out.println();
                result.add(new researchproject.models.Triple(triple.getSubject().toString(),triple.getPredicate().toString(),triple.getObject().toString()));
            }
        }
        finally  {
            qExecutor.close();
        }
        return result;
    }   
    public static List<String> getVariables(String queryString) {
        
       // String queryString = QueryParser.getQuery();
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qExecutor = QueryExecutionFactory.create(query,SparqlService.getGSchemaInstance());
        List<String> result = new ArrayList<String>();
        try {
            ResultSet results =qExecutor.execSelect();
            while(results.hasNext()) {
                result.add(results.next().toString());
            }
        }
        finally  {
            qExecutor.close();
        }
        return result;
    }
}
