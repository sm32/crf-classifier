package com.sm.ds;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

/**
 * Created by mcsri on 6/5/2017.
 */
@SuppressWarnings("ALL")
public class tokenizer {

    private static String WORKING_DIRECTORY = "C:\\Users\\mcsri\\Downloads\\stanford-ner-2016-10-31";
    private static String FILE = "";
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    /**
     * Main function which initiates tokenization
     * @param file (text file required to be tokenized)
     * @throws IOException
     */
    public void execute(String file) throws IOException{
        FILE = file;

        List<String> lines = Files.readAllLines(Paths.get(file), ENCODING);
        List<String> cleanLines = documentCleaner(lines);

        String line = String.join("\n",cleanLines);
        createTokens(line);
    }

    /**
     * Created tokens provided with a clean string
     * @param documentString (Clean file as string)
     * @throws IOException
     */
    private static void createTokens(String documentString) throws IOException {

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        props.setProperty("ssplit.eolonly", "true");
        props.setProperty("untokenizable","noneDelete");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation doc = new Annotation(documentString);
        pipeline.annotate(doc);

        List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> tokens = new ArrayList<String>();
        for(CoreMap s: sentences) {
            for(CoreLabel t: s.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = t.get(CoreAnnotations.TextAnnotation.class).toLowerCase();
                word = word +"\tO";
                tokens.add(word);
            }
            tokens.add("");
        }

        Path file = Paths.get(FILE+".tok");
        Files.write(file, tokens, ENCODING);
        System.out.println("Number of tokens: "+ tokens.size());

    }

    /**
     * Remove/replace special characters in a document
     * @param documents (List of documents)
     * @return
     */
    private static List<String> documentCleaner(List<String> documents) {

        List<String> result = new ArrayList<String>();
        for(String s: documents){
            String r = s.replace(","," ");
            r = r.replace("?"," ");
            r = r.replace("-"," ");
            r = r.replace(";"," ");
            r = r.replace("\"\""," inches ");
            r = r.replace("\""," feet ");
            for(String w: r.split(" ")) {
                if(w.split("\\/([a-zA-Z])").length > 1) {
                    r = r.replace(w, w.replace("/"," "));
                }

                if(org.apache.commons.lang3.StringUtils.isAlphanumeric(w)) {
                   r = r.replace(w, String.join(" ",w.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")));
                }
            }
            r = r.replace("  "," ");
            result.add(r);
        }

        return result;
    }
}
