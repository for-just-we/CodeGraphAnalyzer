package org.example.utils;

import com.google.gson.Gson;
import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.processing.IStrategy;
import de.fraunhofer.aisec.cpg.processing.strategy.Strategy;
import kotlin.Pair;
import org.example.schemes.CPG;
import org.example.visitors.ASTNodeCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToJsonUtil {
    public static Map<String, Object> serializeNode(Node t) {
        int line = -1;
        if (t.getLocation() != null)
            line = t.getLocation().getRegion().startLine;

        ASTNodeCollector nodeCollector = new ASTNodeCollector();
        IStrategy<Node> strategy = Strategy.INSTANCE::AST_FORWARD;
        t.accept(strategy, nodeCollector);

        List<List<String>> contents = new ArrayList<>();
        List<List<Integer>> edges = new ArrayList<>();

        for (Node node: nodeCollector.nodes) {
            int idx = nodeCollector.nodes.indexOf(node);
            String nodeType = node.getClass().getSimpleName();
            String nodeCode = node.getCode();
            contents.add(List.of(nodeType, nodeCode));

            for (Node child: node.getAstChildren()) {
                int childIdx = nodeCollector.nodes.indexOf(child);
                if (childIdx != -1)
                    edges.add(List.of(idx, childIdx));
            }
        }

        return Map.of("line", line, "edges", edges, "contents", contents);
    }

    public static Map<String, Object> serializeCPG(CPG cpg) {
        List<Map<String, Object>> serializedStmts = cpg.statements().stream().map(ToJsonUtil::serializeNode).toList();
        Gson gson = new Gson();
        List<String> serializedNodes = serializedStmts.stream().map(gson::toJson).toList();

        List<List<Integer>> convertedCFGEdges = cpg.cfgEdges().stream().map(edge ->
                List.of(edge.getFirst(), edge.getSecond())).toList();
        List<List<Integer>> convertedCDGEdges = cpg.cdgEdges().stream().map(edge ->
                List.of(edge.getFirst(), edge.getSecond())).toList();
        List<List<Integer>> convertedDFGEdges = cpg.dfgEdges().stream().map(edge ->
                List.of(edge.getFirst(), edge.getSecond())).toList();

        List<String> seiralizedCFGEdges = convertedCFGEdges.stream().map(gson::toJson).toList();
        List<String> seiralizedCDGEdges = convertedCDGEdges.stream().map(gson::toJson).toList();
        List<String> seiralizedDDGEdges = convertedDFGEdges.stream().map(gson::toJson).toList();

        return Map.of("fileName", cpg.fileName(),
                "funcName", cpg.functionName(),
                "nodes", serializedNodes,
                "cfgEdges", seiralizedCFGEdges,
                "cdgEdges", seiralizedCDGEdges,
                "ddgEdges", seiralizedDDGEdges);
    }
}
