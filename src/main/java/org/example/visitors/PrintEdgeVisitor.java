package org.example.visitors;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.graph.edge.PropertyEdge;
import de.fraunhofer.aisec.cpg.processing.IVisitor;
import org.example.schemes.Edge;
import org.example.utils.ParentUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class PrintEdgeVisitor extends IVisitor<Node> {
    public Set<Edge> cdgEdgeSet;
    private final ParentUtil parentUtil;

    public PrintEdgeVisitor(ParentUtil parentUtil) {
        this.cdgEdgeSet = new HashSet<>();
        this.parentUtil = parentUtil;
    }

    @Override
    public void visit(@NotNull Node t) {
        visitEOG(t);
        visitDFG(t);
        visitCDG(t);
        System.out.println("==============================");
        super.visit(t);
    }

    private void visitEOG(Node n) {
        if (n.getLocation() == null)
            return;
        if (n.getNextEOG().size() == 0)
            return;
        System.out.printf("parsing for Node: %s:%s\n", n.getClass().getSimpleName(), n.getCode());
        for (Node nextEOG: n.getNextEOG()) {
            if (nextEOG.getLocation() == null)
                continue;
            System.out.printf("evaluation order:  %s:%s:(%s) --------> %s:%s:(%s)\n", n.getClass().getSimpleName(),
                    n.getCode(), n.getLocation().getRegion(),
                    nextEOG.getClass().getSimpleName(), nextEOG.getCode(),
                    nextEOG.getLocation().getRegion());
        }
    }

    private void visitCDG(Node n) {
        for (PropertyEdge<Node> cdgEdge: n.getNextCDGEdges()) {
            System.out.printf("control dependence: %s:%s --------> %s:%s\n", cdgEdge.getStart().getClass().getSimpleName(),
                    cdgEdge.getStart().getCode(), cdgEdge.getEnd().getClass().getSimpleName(), cdgEdge.getEnd().getCode());

            Node topLevelExprPrev = this.parentUtil.getTopLevelExpression(cdgEdge.getStart());
            Node topLevelExprSucc = this.parentUtil.getTopLevelExpression(cdgEdge.getEnd());

            if (topLevelExprPrev != null && topLevelExprSucc != null && topLevelExprPrev != topLevelExprSucc) {
                cdgEdgeSet.add(new Edge(topLevelExprPrev, topLevelExprSucc));
            }
        }
    }

    private void visitDFG(Node n) {
        if (n.getLocation() == null)
            return;
        if (n.getPrevDFG().size() == 0)
            return;
        System.out.printf("parsing for Node: %s:%s\n", n.getClass().getSimpleName(), n.getCode());
        for (Node prevDFG: n.getPrevDFG()) {
            if (prevDFG.getLocation() == null)
                continue;
            System.out.printf("data flow:  %s:%s:(%s) --------> %s:%s:(%s)\n", prevDFG.getClass().getSimpleName(),
                    prevDFG.getCode(), prevDFG.getLocation().getRegion(),
                    n.getClass().getSimpleName(), n.getCode(), n.getLocation().getRegion());
        }
    }
}
