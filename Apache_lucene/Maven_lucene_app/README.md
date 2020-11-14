# cs7is3-lucene-tutorial-examples

These are example scripts as outlined in CS7IS3 (TCD) Lucene Tutorial

These scripts are a good starting point executing Assignment.

Pre-requisite Step:

1. Navigate to the the folder containing pom.xml file. 
2. Run "cd /Apache_lucene/Maven_lucene_app 

Execution Steps:

Steps to install all neccessary package using maven: 

1. Run "mvn clean"
2. Build and compile the project: "mvn package"

General intoduction to lucene application created.

3. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.Intro"

Steps to create intial index file and updating it with the index file of cranfield dataset.

4. Run "rm -r index". This can be done before you tried to index file.
5. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.CreateIndex cran/cran.all.1400"

Steps to Query the cran.qry file to extract the relevent documents. [Perform ]
[Perform different relevance check]
	
	0 -> VSM
6[a]. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.QueryIndex 0"
	
	1 -> BM25

6[b]. Run "java -cp target/Maven_lucene_app-0.0.1-SNAPSHOT.jar ir.josh.Maven_lucene_app.QueryIndex 1"

Steps to implement performance evaluvation using trec-eval.

7. cd  trec-eval-9.0.7/
8. Run ./trec_eval ../cran/QRelsCorrectedforTRECeval ../output/output_text_file.txt

.......................................THE END.................................................................
