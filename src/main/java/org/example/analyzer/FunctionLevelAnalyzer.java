package org.example.analyzer;

import de.fraunhofer.aisec.cpg.ConfigurationException;
import de.fraunhofer.aisec.cpg.TranslationResult;
import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.processing.IStrategy;
import de.fraunhofer.aisec.cpg.processing.strategy.Strategy;
import org.example.utils.CPGBuilder;
import org.example.utils.ToJsonUtil;
import org.example.visitors.EdgeCollectPerFunctionVisitor;
import org.example.visitors.TopLevelExpressionCollectVisitor;
import org.example.visitors.TraversingVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FunctionLevelAnalyzer {
    private List<String> fileNames;

    public List<Map<String, Object>> serializedCPGs;

    public FunctionLevelAnalyzer(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public void analyze() throws ConfigurationException, ExecutionException, InterruptedException {
        List<File> files = this.fileNames.stream().map(File::new).toList();
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

        this.serializedCPGs = builder.cpgs.stream().map(ToJsonUtil::serializeCPG).toList();
    }
}
