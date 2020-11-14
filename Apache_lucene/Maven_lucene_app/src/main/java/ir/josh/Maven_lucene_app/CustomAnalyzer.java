package ir.josh.Maven_lucene_app;

//Importing all neccessary Java general library packages.
import java.util.*;

//Importing all neccessary Lucene library Packages.

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.util.AbstractAnalysisFactory;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.PorterStemFilter;


//Creating custom analyzer for both indexing and searching purpose.
public class CustomAnalyzer extends Analyzer{
  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
	//Tokenizer whitespaceTokenizer  = new WhitespaceTokenizer();
	Tokenizer stdTokenizer = new StandardTokenizer();
	TokenStream filter = new LowerCaseFilter(stdTokenizer);
    //Reference:from https://github.com/Yoast/YoastSEO.js/blob/develop/src/config/stopwords.js
	List<String> stopWordList = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", ".","our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves","a");
	CharArraySet stopWordSet = new CharArraySet( stopWordList, true);
	TokenStream filter1 = new StopFilter(filter, stopWordSet);
	filter = new PorterStemFilter(filter1);
	return new TokenStreamComponents(stdTokenizer, filter);
  }
}

//THE END.
