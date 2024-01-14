package org.example.utils;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.graph.declarations.FunctionDeclaration;
import kotlin.Pair;
import org.example.schemes.CPG;
import org.example.schemes.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CPGBuilder {
    private final ParentUtil parentUtil;

    private final Map<FunctionDeclaration, List<Node>> topLevelExpressions;

    public List<CPG> cpgs;

    private final Set<Edge> cdgEdgeSet;

    private final Set<Edge> cfgEdgeSet;

    private final Set<Edge> dfgEdgeSet;

    public CPGBuilder(ParentUtil parentUtil,
                      Map<FunctionDeclaration, List<Node>> topLevelExpressions,
                      Set<Edge> cfgEdgeSet, Set<Edge> cdgEdgeSet, Set<Edge> dfgEdgeSet) {
        this.parentUtil = parentUtil;
        this.topLevelExpressions = topLevelExpressions;
        this.cfgEdgeSet = cfgEdgeSet;
        this.cdgEdgeSet = cdgEdgeSet;
        this.dfgEdgeSet = dfgEdgeSet;
        this.cpgs = new ArrayList<>();
    }

    public void buildCPG() {
        topLevelExpressions.forEach((this::buildCPGForFunction));
    }

    private void buildCPGForFunction(FunctionDeclaration functionDeclaration,
                                     List<Node> statements) {
        String funcName = functionDeclaration.getName().getLocalName();
        String fileName = functionDeclaration.getFile();

        List<Pair<Integer, Integer>> cfgEdges = new ArrayList<>();
        List<Pair<Integer, Integer>> cdgEdges = new ArrayList<>();
        List<Pair<Integer, Integer>> dfgEdges = new ArrayList<>();

        processEdges(statements, this.cfgEdgeSet, cfgEdges);
        processEdges(statements, this.cdgEdgeSet, cdgEdges);
        processEdges(statements, this.dfgEdgeSet, dfgEdges);

        CPG cpg = new CPG(funcName, fileName, statements, cfgEdges, cdgEdges, dfgEdges);
        cpgs.add(cpg);
    }

    private void processEdges(List<Node> statements, Set<Edge> edgeSet, List<Pair<Integer, Integer>> resultEdges) {
        for (Edge edge : edgeSet) {
            int startIdx = statements.indexOf(edge.start());
            int endIdx = statements.indexOf(edge.end());

            if (startIdx != -1 && endIdx != -1)
                resultEdges.add(new Pair<>(startIdx, endIdx));
        }
    }
}
