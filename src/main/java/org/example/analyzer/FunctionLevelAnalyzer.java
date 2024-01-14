package org.example.analyzer;

import de.fraunhofer.aisec.cpg.ConfigurationException;
import de.fraunhofer.aisec.cpg.TranslationResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FunctionLevelAnalyzer {
    private List<String> fileNames;

    public FunctionLevelAnalyzer(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public void analyze() throws ConfigurationException, ExecutionException, InterruptedException {
        List<File> files = this.fileNames.stream().map(File::new).toList();
        TranslationResult translationResult = TranslationLoader.build(files);
    }
}
