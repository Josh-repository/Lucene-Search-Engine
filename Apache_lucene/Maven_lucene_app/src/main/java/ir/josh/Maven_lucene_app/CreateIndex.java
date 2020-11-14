package ir.josh.Maven_lucene_app;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.*;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.Query;


public class CreateIndex{
	
	private CreateIndex() {};
	
	private static String Index_Directory = "index";
	
	public static String docs_path="";
	
	public static void main(String[] args){
	
		// Make sure we were given something to index
		if (args.length <= 0)
		{
            System.out.println("Expected corpus as input");
            System.exit(1);            
        }
		else
		{
			docs_path = args[0];
		}
	//capturing the final path of the cran dataste
	final Path doc = Paths.get(docs_path);
    if (!Files.isReadable(doc)) {
      System.out.println("Document directory '" +doc.toAbsolutePath()+ "' is not readable. Try to give another file path or check the vaildity of the given path");
      System.exit(1);
    }	
	Date index_start = new Date();
    try {
		
		// Open the directory that contains the search index
		Directory directory = FSDirectory.open(Paths.get(Index_Directory));
		
		// Analyzer that is used to process TextField
		Analyzer analyzer = new CustomAnalyzer();
		
		// Set up an index writer to add process and save documents to the index
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iwriter = new IndexWriter(directory, config);
		
		createindex(iwriter, doc);
		//closing things whatever we opened.
        iwriter.close();
		directory.close();

        Date index_end = new Date();
        // Printing the time elapsed.
        System.out.println(index_end.getTime() - index_start.getTime() + " total milliseconds");
	}
    //throwing any exception if occured.
	catch (IOException e) {
      System.out.println(" Error: " + e.getClass() +
       "\n discription " + e.getMessage());
    }
}
//Since our given dataset is a single document. executing code to index the file.	
 public static void createindex(IndexWriter iwriter, Path doc) throws IOException{
	 try (InputStream x = Files.newInputStream(doc)){
	    BufferedReader br = new BufferedReader(new InputStreamReader(x, StandardCharsets.UTF_8));
        String liveline = br.readLine();
        System.out.println(liveline);
		Document document;
		String Type_document1 = "";String Type_document2 = "";String Type_document3 = "";String Type_document4 = "";
		
		 
		while(liveline != null) {
		  if(liveline.matches("(\\.I)( )(\\d)*")){
          
		  document = new Document();
		  //System.out.println(liveline + "in");
          Field pathField = new StringField("path", liveline, Field.Store.YES);
          System.out.println(pathField + "yes");
		  String nextLine ="";
		  document.add(pathField);
          liveline = br.readLine();
          while(!(liveline.matches("(\\.I)( )(\\d)*"))){	                             //Quering over entire document.
            if(liveline.matches("(\\.T)")){
              Type_document1 = "Title";
              liveline = br.readLine();
			while(!(liveline.matches("\\.A")))
			  {
				nextLine = nextLine + "" + liveline;
				liveline=br.readLine();
			  }
				document.add(new TextField("Title", nextLine, Field.Store.YES));
				
				} 
			else if(liveline.matches("(\\.A)")){
              liveline = br.readLine();
			  while(!(liveline.matches("\\.B")))
			  {
				nextLine= nextLine + "" + liveline;
				liveline=br.readLine();
			  }
              document.add(new TextField("Author", nextLine, Field.Store.YES));
			  } 			  
            else if(liveline.matches("(\\.B)")){
              //Type_document  = "Bibliography";
              liveline = br.readLine();
              while(!(liveline.matches("\\.W")))
			  {
				nextLine= nextLine + "" + liveline;
				liveline=br.readLine();
			  }
			document.add(new TextField("Bibliography", nextLine, Field.Store.YES));
			  } 
			else if(liveline.matches("(\\.W)")){
              //Type_document = "Words";
              liveline = br.readLine();
              while(!((liveline.matches("(\\.I)( )(\\d)*"))))
			  {
				nextLine= nextLine + "" + liveline;
				liveline=br.readLine();
				if(liveline == null){
				System.out.println("1");
				break;
				}
				}
				document.add(new TextField("Words", nextLine, Field.Store.YES)); 
			  } 
			 
            if(liveline == null){
              break;
            }
		}
                        System.out.println("Creating " + liveline);
                        //iwriter.updateDocument(new Term("path", doc.toString()), document);
						iwriter.addDocument(document);
          }
        }
      }	
	}
 }
	
	
	
	
	
	
	
	
