package org.example.visitors;

import de.fraunhofer.aisec.cpg.ConfigurationException;
import de.fraunhofer.aisec.cpg.TranslationResult;
import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.processing.IStrategy;
import de.fraunhofer.aisec.cpg.processing.strategy.Strategy;
import org.example.analyzer.TranslationLoader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

class PrintEdgeVisitorTest {
    private final String testFile = "src/test/testfiles/test1.c";

    @Test
    public void test() throws ConfigurationException, ExecutionException, InterruptedException {
        List<String> fileNames = List.of(testFile);
        List<File> files = fileNames.stream().map(File::new).toList();
        TranslationResult translationResult = TranslationLoader.build(files);

        IStrategy<Node> strategy = Strategy.INSTANCE::AST_FORWARD;
        TraversingVisitor traversingVisitor = new TraversingVisitor();
        translationResult.accept(strategy, traversingVisitor);
        PrintEdgeVisitor visitor = new PrintEdgeVisitor(traversingVisitor.parentUtil);
        translationResult.accept(strategy, visitor);
    }
}