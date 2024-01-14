package org.example.visitors;

import de.fraunhofer.aisec.cpg.ConfigurationException;
import de.fraunhofer.aisec.cpg.TranslationResult;
import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.processing.IStrategy;
import de.fraunhofer.aisec.cpg.processing.strategy.Strategy;
import org.example.analyzer.TranslationLoader;
import org.example.utils.CPGBuilder;
import org.example.utils.ToJsonUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class EdgeCollectPerFunctionVisitorTest {
    private final String testFile = "src/test/testfiles/test1.c";

    @Test
    public void test() throws ConfigurationException, ExecutionException, InterruptedException {
        List<String> fileNames = List.of(testFile);
        List<File> files = fileNames.stream().map(File::new).toList();
        TranslationResult translationResult = TranslationLoader.build(files);

        IStrategy<Node> strategy = Strategy.INSTANCE::AST_FORWARD;
        TraversingVisitor traversingVisitor = new TraversingVisitor();
        translationResult.accept(strategy, traversingVisitor);

        TopLevelExpressionCollectVisitor topLevelExpressionCollectVisitor =
                new TopLevelExpressionCollectVisitor(traversingVisitor.parentUtil);
        translationResult.accept(strategy, topLevelExpressionCollectVisitor);

        EdgeCollectPerFunctionVisitor visitor = new EdgeCollectPerFunctionVisitor(traversingVisitor.parentUtil);
        translationResult.accept(strategy, visitor);

        CPGBuilder builder = new CPGBuilder(traversingVisitor.parentUtil,
                                            topLevelExpressionCollectVisitor.topLevelExpressions,
                                            visitor.cfgEdgeSet, visitor.cdgEdgeSet, visitor.dfgEdgeSet);
        builder.buildCPG();

        List<Map<String, Object>> serializedCPGs =
                builder.cpgs.stream().map(ToJsonUtil::serializeCPG).toList();
        System.out.println(serializedCPGs);
    }
}