package org.example.visitors;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.graph.declarations.FunctionDeclaration;
import de.fraunhofer.aisec.cpg.graph.statements.*;
import de.fraunhofer.aisec.cpg.graph.statements.expressions.Block;
import de.fraunhofer.aisec.cpg.processing.IVisitor;
import org.example.schemes.Edge;
import org.example.utils.ParentUtil;

import java.util.*;

public class TopLevelExpressionCollectVisitor extends IVisitor<Node> {
    public Map<FunctionDeclaration, List<Node>> topLevelExpressions;

    private final ParentUtil parentUtil;

    public TopLevelExpressionCollectVisitor(ParentUtil parentUtil) {
        this.topLevelExpressions = new HashMap<>();
        this.parentUtil = parentUtil;
    }

    private boolean isWrapNode(Node node) {
        return node instanceof FunctionDeclaration ||
                node instanceof Block;
    }

    private boolean isConditionNode(Node node) {
        return node instanceof IfStatement ||
                node instanceof SwitchStatement ||
                node instanceof ForStatement ||
                node instanceof WhileStatement ||
                node instanceof DoStatement;
    }

    @Override
    public void visit(Node t) {
        Node funcDeclNode = parentUtil.getFunctionDeclNode(t);
        if (funcDeclNode == null)
            return;
        if (isWrapNode(t) || isConditionNode(t)) {
            for (Node child: t.getAstChildren())
                if (!(isWrapNode(child) || isConditionNode(child))) {
                    topLevelExpressions.computeIfAbsent(
                            (FunctionDeclaration) funcDeclNode, k -> new ArrayList<>()).add(child);
                    parentUtil.topLevelExpressions.add(child);
                }
        }
    }
}
