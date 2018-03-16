/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package researchproject.Queries;

import java.util.ArrayList;

/**
 *
 * THIS CLASS IS RESPONSIBLE FOR RETURNING GLOBAL QUERIES
 */
public class GQueries {
    private static final  ArrayList queries = new ArrayList();
    
    static  {
      //query 3
      queries.add("prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> prefix ex: <http://example.org/federation#> Select ?node2 (xsd:float(?monthCount)/?monthBeforeCount As ?ratio) { { Select ?node2 (count(?node4) As ?monthCount) { ?node4 rdf:type ex:Review . ?node4 ex:reviewFor ?node2 . ?node4 ex:date ?node42 . Filter(?node42 >= \"2008-03-01\"^^<http://www.w3.org/2001/XMLSchema#date> && ?node42 < \"2008-04-01\"^^<http://www.w3.org/2001/XMLSchema#date>) } Group By ?node2 } { Select ?node2 (count(?node4) As ?monthBeforeCount) { ?node4 rdf:type ex:Review . ?node4 ex:reviewFor ?node2 . ?node4 ex:date ?node42 . Filter(?node42 >= \"2008-02-01\"^^<http://www.w3.org/2001/XMLSchema#date> && ?node42 < \"2008-03-01\"^^<http://www.w3.org/2001/XMLSchema#date>) } Group By ?node2 Having (count(?node4)>0) } } Order By desc(xsd:float(?monthCount) / ?monthBeforeCount) ?node2 Limit 10");
      //query 4
      queries.add("prefix ex: <http://example.org/federation#> prefix xsd: <http://www.w3.org/2001/XMLSchema#> prefix qb: <http://purl.org/linked-data/cube#> prefix gs: <http://example.org/globalschema#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> Select ?feature ((?sumF*(?countTotal-?countF))/(?countF*(?sumTotal-?sumF)) As ?priceRatio) { { Select (count(?price) As ?countTotal) (sum(xsd:float(str(?price))) As ?sumTotal) { ?product a <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType45> . ?offer rdf:type qb:Observation . ?offer qb:DataSet gs:OfferDataset . ?offer ex:product ?product . ?offer ex:productPrice ?price . } } { Select ?feature (count(?price) As ?countF) (sum(xsd:float(str(?price))) As ?sumF) { ?product2 a <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType45> . ?product2 ex:productFeature ?feature . ?offer rdf:type qb:Observation . ?offer qb:DataSet gs:OfferDataset . ?offer ex:product ?product2 . ?offer ex:productPrice ?price . ?feature rdf:type ex:ProductFeature. } Group By ?feature } } Order By desc(?priceRatio) ?feature Limit 100");
     //query 5
      queries.add("prefix bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> prefix bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/> prefix rev: <http://purl.org/stuff/rev#> prefix xsd: <http://www.w3.org/2001/XMLSchema#> prefix ex: <http://example.org/federation#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> prefix skos: <http://www.w3.org/2004/02/skos/core#> prefix qb: <http://purl.org/linked-data/cube#> prefix gs: <http://example.org/globalschema#> Select ?country ?product ?nrOfReviews ?avgPrice { { Select ?country (max(?nrOfReviews) As ?maxReviews) { { Select ?country ?product (count(?review) As ?nrOfReviews) { ?product a <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType45> . ?review ex:reviewFor ?product . ?review rdf:type ex:Review . ?review ex:reviewer ?reviewer . ?reviewer rdf:type ex:Person. ?reviewer skos:broader ?city . ?city skos:broader ?country . } Group By ?country ?product } } Group By ?country } { Select ?product (avg(xsd:float(str(?price))) As ?avgPrice) { ?product a <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType45> . ?offer ex:product ?product . ?offer rdf:type qb:Observation . ?offer qb:DataSet gs:OfferDataset . ?offer ex:productPrice ?price . } Group By ?product } { Select ?country ?product (count(?review) As ?nrOfReviews) { ?product a <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType45> . ?review ex:reviewFor ?product . ?review rdf:type ex:Review . ?review ex:reviewer ?reviewer . ?reviewer rdf:type ex:Person. ?reviewer skos:broader ?city . ?city skos:broader ?country . } Group By ?country ?product } FILTER(?nrOfReviews=?maxReviews) } Order By desc(?nrOfReviews) ?country ?product Limit 100");  
      //Query 6
      queries.add("prefix bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> prefix bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/> prefix ex: <http://example.org/federation#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> Select ?reviewer (avg(xsd:float(?score)) As ?reviewerAvgScore) { { Select (avg(xsd:float(?score)) As ?avgScore) { ?product bsbm:producer <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer9/Producer9> . ?review ex:reviewFor ?product . ?review rdf:type ex:Review . { ?review ex:rating1 ?score . } UNION { ?review ex:rating2 ?score . } UNION { ?review ex:rating3 ?score . } UNION { ?review ex:rating4 ?score . } } } { Select ?reviewer ?score Where { ?product bsbm:producer <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer9/Producer9> . ?review ex:reviewFor ?product . ?review rdf:type ex:Review . ?review ex:reviewer ?reviewer . ?reviewer rdf:type ex:Person. { ?review ex:rating1 ?score . } UNION { ?review ex:rating2 ?score . } UNION { ?review ex:rating3 ?score . } UNION { ?review ex:rating4 ?score . } } } } Group By ?reviewer Having (avg(xsd:float(?score)) > min(?avgScore) * 1.5) LIMIT 100");
      //query 7
      queries.add("prefix skos: <http://www.w3.org/2004/02/skos/core#> prefix bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> prefix ex: <http://example.org/federation#> prefix xsd: <http://www.w3.org/2001/XMLSchema#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> prefix qb: <http://purl.org/linked-data/cube#>  prefix qb4o: <http://purl.org/olap#>  prefix gs: <http://example.org/globalschema#> Select ?product { { Select ?product (count(?offer) As ?offerCount) { ?offer rdf:type qb:Observation . ?offer qb:DataSet gs:OfferDataset . ?offer ex:product ?product . ?offer ex:vendor ?vendor . ?vendor rdf:type bsbm:Vendor . ?vendor skos:broader ?city . ?city skos:broader ?country . ?product a <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType19> . FILTER(?country!=<http://downlode.org/rdf/iso-3166/countries#UK>) } Group By ?product Order By desc(?offerCount) } } Limit 1000");
      //query 8
      queries.add("prefix ex: <http://example.org/federation#>prefix xsd: <http://www.w3.org/2001/XMLSchema#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> prefix qb: <http://purl.org/linked-data/cube#>  prefix qb4o: <http://purl.org/olap#>  prefix gs: <http://example.org/globalschema#> Select ?vendor (xsd:float(?belowAvg)/?offerCount As ?cheapExpensiveRatio) { { Select ?vendor (count(?offer) As ?belowAvg) { { { Select ?vendor ?product ?price ?offer WHERE { ?offer rdf:type qb:Observation . ?offer qb:DataSet gs:OfferDataset . ?offer ex:product ?product . ?offer ex:vendor ?vendor . ?offer ex:productPrice ?price . ?product rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType19> . } } { Select ?product (avg(xsd:float(str(?price))) As ?avgPrice) { ?offer rdf:type qb:Observation . ?offer qb:DataSet gs:OfferDataset . ?offer ex:product ?product . ?offer ex:vendor ?vendor . ?offer ex:productPrice ?price . ?product rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType19> . } Group By ?product } } FILTER (xsd:float(str(?price)) < ?avgPrice) } Group By ?vendor } { Select ?vendor (count(?offer) As ?offerCount) { ?offer rdf:type qb:Observation . ?offer qb:DataSet gs:OfferDataset . ?offer ex:product ?product . ?offer ex:vendor ?vendor . ?product a <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType19> . } Group By ?vendor } } Order by desc(xsd:float(?belowAvg)/?offerCount) ?vendor limit 10");
     }
    
    
    public static String getQuery(int queryId){
        return queries.get(queryId).toString();
    }
}