package ir.josh.Maven_lucene_app;

//Importing the required java packages.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;

//Importing all neccessary lucene package.
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import java.util.*;

public class QueryIndex{
	
	private QueryIndex() {};
	// the location of the search index and query file.
	public static String INDEX_DIRECTORY = "index";
	public static String query = "cran/cran.qry";

	
	// Limit the number of search results per page.
	public static int MAX_RESULTS = 10;
	public static int scoringType = 0;
	public static String queryString = null;
	// output string to display the results.
	public static String outputs = "";
	
	public static void main(String[] args) throws IOException, ParseException
	{
		// Make sure we were given something to index
		if (args.length <= 0)
		{
            System.out.println("Expected query file as input");
            System.exit(1);            
        }
		else
		{
			scoringType = Integer.parseInt(args[0]);
			
		}
	final Path query_path = Paths.get(query);
	System.out.println(scoringType);
    if (!Files.isReadable(query_path)) {
      System.out.println("Query document '" +query_path.toAbsolutePath()+ "' is not readable. Try to give another file path or check the vaildity of the given path");
      System.exit(1);
	}
		BufferedReader input = null;
		input = Files.newBufferedReader(Paths.get(query), StandardCharsets.UTF_8);
		// Open the folder that contains our search index
		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
		
		// create objects to read and search across the index
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);	
		// Making use of custom analyzer.
		Analyzer analyzer = new CustomAnalyzer();
		
		// Using if to select what type of scoring method we need to work upon
	    if(scoringType == 0) isearcher.setSimilarity(new ClassicSimilarity());
		if(scoringType == 1) isearcher.setSimilarity(new BM25Similarity());

		// Hashmap technique to boost certainf field value upon quering. Tried to execute it while indexing but that feature was scrapped in latest linux version.
		HashMap<String, Float> setboost = new HashMap<String, Float>();
		setboost.put("Title", 0.25f);
		setboost.put("Words", 2.5f);
		setboost.put("Author", 0.02f);
		setboost.put("Bibliography", 0.01f);
		MultiFieldQueryParser parser = new MultiFieldQueryParser (new String[] {"Title","Words","Author","Bibliography"},analyzer, setboost);
		//QueryParser parser = new QueryParser ( String "Title", Analyzer analyzer);
	//Analyzing the cran.qry dataset.
	String line=input.readLine();
	String nextLine ="";
	int queryNumber = 1;
	PrintWriter exporter = new PrintWriter("output/output_text_file.txt", "UTF-8");
    while (line!=null) {
	if (line == null || line.length() == -1) {
      	break;
      }
	  line = line.trim();
      if (line.length() == 0) {
        break;
      }
	//Checkin the query file for index values so that it can be skipped.
	String test=line.substring(0,2);
	if( test.equals(".I") ){
		line = input.readLine();	  	
		if( line.equals(".W") ){
			line = input.readLine();
		}
		nextLine = "";
		while( !(line.substring(0,2).equals(".I")) ){
			nextLine = nextLine + " " + line;
			line = input.readLine();
			if( line == null ) break;
		}
	  }
	Query query1 = parser.parse(QueryParser.escape(nextLine.trim()));
	Scoring_function(queryNumber, input, isearcher, query1, MAX_RESULTS, query == null && queryString == null, exporter);
	queryNumber++;
	}
	
	// close everything we opened.
	exporter.close();
	ireader.close();
	directory.close();
}


private static void Scoring_function(int queryNumber, BufferedReader input, IndexSearcher searcher, Query query1, int hitsPerPage, boolean interactive, PrintWriter writer) throws IOException {

	TopDocs search_output = searcher.search(query1, 5 * hitsPerPage);
	int num = Math.toIntExact(search_output.totalHits.value);
	search_output = searcher.search(query1, num);
	ScoreDoc[] hits = search_output.scoreDocs;
	// Print the results
	System.out.println("Documents: " + hits.length);
    int start = 0;
    int end = Math.min(hits.length, hitsPerPage);
        
    while (true) {      
      end = Math.min(hits.length, start + hitsPerPage);
      for (int i = start; i < num; i++) {
        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
			System.out.println(queryNumber + " 0 " + path.replace(".I ","") + " " +(i+1)+ " " + hits[i].score);		  
			writer.println(queryNumber+" 0 " + path.replace(".I ","") + " " + (i+1) + " " + hits[i].score +" EXP");
		}    
      }
      if (!interactive || end == 0) {
        break;
      }
    }
  }
} 
// THE ULTIMATE END.