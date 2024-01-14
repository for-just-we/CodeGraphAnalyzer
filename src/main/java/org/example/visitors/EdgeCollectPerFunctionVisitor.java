package org.example.visitors;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.processing.IVisitor;
import org.example.schemes.Edge;
import org.example.utils.ParentUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class EdgeCollectPerFunctionVisitor extends IVisitor<Node> {
    public Set<Edge> cdgEdgeSet;

    public Set<Edge> cfgEdgeSet;

    public Set<Edge> dfgEdgeSet;

    private final ParentUtil parentUtil;

    public EdgeCollectPerFunctionVisitor(ParentUtil parentUtil) {
        this.cdgEdgeSet = new HashSet<>();
        this.cfgEdgeSet = new HashSet<>();
        this.dfgEdgeSet = new HashSet<>();
        this.parentUtil = parentUtil;
    }

    private void addEdge(Node topLevelExprPrev, Node topLevelExprSucc, Set<Edge> edgeSet) {
        if (topLevelExprPrev != null && topLevelExprSucc != null && topLevelExprPrev != topLevelExprSucc) {
            edgeSet.add(new Edge(topLevelExprPrev, topLevelExprSucc));
        }
    }

    @Override
    public void visit(@NotNull Node t) {
        visitEOG(t);
        visitDFG(t);
        visitCDG(t);
        super.visit(t);
    }

    // cfg
    private void visitEOG(Node n) {
        Node topLevelExprPrev = this.parentUtil.getTopLevelExpression(n);
        if (topLevelExprPrev == null)
            return;
        for (Node nextEOG: n.getNextEOG()) {
            Node topLevelExprSucc = this.parentUtil.getTopLevelExpression(nextEOG);
            addEdge(topLevelExprPrev, topLevelExprSucc, this.cfgEdgeSet);
        }
    }

    // cdg
    private void visitCDG(Node n) {
        Node topLevelExprPrev = this.parentUtil.getTopLevelExpression(n);
        if (topLevelExprPrev == null)
            return;
        for (Node nextCDG: n.getNextCDG()) {
            Node topLevelExprSucc = this.parentUtil.getTopLevelExpression(nextCDG);
            addEdge(topLevelExprPrev, topLevelExprSucc, this.cdgEdgeSet);
        }
    }

    // dfg
    private void visitDFG(Node n) {
        Node topLevelExprPrev = this.parentUtil.getTopLevelExpression(n);
        if (topLevelExprPrev == null)
            return;
        for (Node nextDFG: n.getNextDFG()) {
            Node topLevelExprSucc = this.parentUtil.getTopLevelExpression(nextDFG);
            addEdge(topLevelExprPrev, topLevelExprSucc, this.dfgEdgeSet);
        }
    }
}
