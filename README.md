# Lucene-Search-Engine
Indexing and quering over Cranfield Dataset [ Java implementation ]

# Problem Statement:
The task is to develop indexing and searching java programs for implementing a search engine with the help of Lucene libraries. The given dataset contains a query file that can be used to query upon a Cranfield Data once after indexing it. Later, the performance of the search engine built was calculated against different scoring options using the Trec_eval tool.
Techniques Involved:
# Data processing and analyzing:
The provided datasets consist of contents written in English language which comes along with stop words like ‘and’, ‘of ’, ‘in’, and so on. In addition it also contains words in ‘gerund’, ‘plural’ and different tense forms. So before indexing part it is important to choose what analyzer fits best[for e.g. Analyzer that removes stop word, does stemming and keeps number and special characters] for the given dataset.
Standard Analyzer - Maybe-Removes leading dots, plural, and stop words.  
Simple Analyzer-No-Doesn’t remove stop words. It removes numbers and special characters.  
Whitespace Analyzer-No-Only removes white space between terms. So not efficient.  
Stop Analyzer-No-Remove stop words, but also the numbers and special characters. So not good for Cranfield Dataset.  
Based on above comparison, it is good to make use of Standard Analyzer.
# Issue in implementing Standard analyzer:
•	Removes only default stop words that are predefined.  
•	It removes leading‘s’ but no other gerund or different tense of the word.  
# Custom Analyzer: 
In order to overcome the above issues we make use of custom analyzer. It is built by  
•	Creating a Standard Tokenizer named stdtokenizer.      
•	Assigning the tokenizer to a token stream named filter to perform operations like lower case conversion, custom built stop word removal and implementing porter stem filter for the purpose of stemming.  [Through this the problem in Standard Analyzer can be tackled and performance efficiency can be improved]    
# Indexing: 
Lucene makes use of inverted index concept. So indexing our given dataset is quite tricky, because all the 1400 documents are placed in a single file separated header ‘I.’ Buffered reader package of java comes in handy to solve this issue of segregating 1400 documents. Then with the help of Directory and Indexwriter package each term of the documents are inverted indexed into the index folder.

# Searching:
In case of searching, the query file is similar to that of Cran data. So Custom analyzer finds its application here. Second, while indexing I made use of several field therefore it is wise to use Multifield query parser instead of querying over single field. Since the query file contains term which mostly occur in ‘Words’ field, so I set boost score of 2.5f for ‘Words’ by using Hash function. This score actually specifies the engine to give more importance to ‘Words’ field while querying. Similarly 0.25f for ‘title’, 0.02f for ‘Author’, 0.01 for ‘Bibliography ’.  These values are finalized by doing trial and error method by setting different boost scores.

# Scoring:
I opted for traditional TF-IDF [Classic Similarity] and BM25 to check the relevance of documents retrieved for each query and scoring values are noted and compared. BM25 is formed by modifying the TF-IDF function.
Formula: TF-IDF is IDF score * TF score * fieldNorms
Formula: BM25 is IDF * ((k + 1) * tf) / (k * (1.0 - b + b * (|d|/avgDl)) + tf)
TF: No of times term appear in a document and IDF: No of documents that contain the term.

# Execution steps:
These are example scripts as outlined in CS7IS3 (TCD) Lucene Tutorial
These scripts are a good starting point executing Assignment.
1. Navigate to the the folder containing pom.xml file.
2. Run "cd /Apache_lucene/Maven_lucene_app
Steps to install all neccessary package using maven:

3. Run "mvn clean"
4. Build and compile the project: "mvn package"
5. General intoduction to lucene application created.

6. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.Intro"
7. Steps to create intial index file and updating it with the index file of cranfield dataset.

8. Run "rm -r index". This can be done before you tried to index file.
9. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.CreateIndex cran/cran.all.1400"
10. Steps to Query the cran.qry file to extract the relevent documents. [Perform ] [Perform different relevance check]

0 -> VSM
6[a]. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.QueryIndex 0"

1 -> BM25
6[b]. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.QueryIndex 1"

11.Steps to implement performance evaluvation using trec-eval.  
11a. cd trec-eval-9.0.7  
11b. Run ./trec_eval ../cran/QRelsCorrectedforTRECeval ../output/output_text_file.txt

# Results:
On comparing the Mean value precision,P_5 and Recall values of both standard analyzer and custom analyzer it is clear that custom analyzer performs better than standard analyzer [For example ‘map’ value of Standard analyzer 0.3607 is lesser than 0.4052 of Custom Analyzer]. Similarly BM25 scoring function perform betters than classic similarity in calculating the relevance of the document retrieved. 

