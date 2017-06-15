package com.sm.ds;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.sequences.SeqClassifierFlags;

import java.io.*;
import java.util.Properties;

/**
 * Created by mcsri on 6/5/2017.
 */
@SuppressWarnings("ALL")
public class classifier {

    /**
     * Train the model, output is a serialized object written to file
     * @param prop (configuration file)
     * @throws IOException
     */
    public void train(Properties prop) throws IOException {
        SeqClassifierFlags flags = new SeqClassifierFlags(prop);
        CRFClassifier<CoreLabel> clf = new CRFClassifier<CoreLabel>(flags);
        clf.train(prop.getProperty("trainFile"));
        clf.serializeClassifier(prop.getProperty("serializeTo"));
    }

    /**
     * Testing based on the trained model
     * @param prop (configuration file)
     * @throws Exception
     */
    public void test(Properties prop) throws Exception {
        String testFile = prop.getProperty("testFile");

        CRFClassifier clf = CRFClassifier.getClassifier(prop.getProperty("serializeTo"));
        DocumentReaderAndWriter reader=clf.makeReaderAndWriter();
        clf.classifyAndWriteAnswers(testFile, reader,true);

    }

}
