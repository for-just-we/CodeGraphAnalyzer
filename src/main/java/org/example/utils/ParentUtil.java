package org.example.utils;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.graph.declarations.FunctionDeclaration;
import de.fraunhofer.aisec.cpg.graph.statements.DoStatement;
import de.fraunhofer.aisec.cpg.graph.statements.ForStatement;
import de.fraunhofer.aisec.cpg.graph.statements.IfStatement;
import de.fraunhofer.aisec.cpg.graph.statements.WhileStatement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParentUtil {
    private Map<Node, Node> parentMap;

    public Set<Node> topLevelExpressions;

    public ParentUtil() {
        this.parentMap = new HashMap<>();
        this.topLevelExpressions = new HashSet<>();
    }

    public void addParentRelation(Node child, Node parent) {
        parentMap.put(child, parent);
    }

    public Node getFunctionDeclNode(Node node) {
        while (node != null && !(node instanceof FunctionDeclaration))
            node = parentMap.getOrDefault(node, null);
        return node;
    }

    // 是否属于top level expression
    public boolean isTopLevelExpression(Node node) {
        return topLevelExpressions.contains(node);
    }

    public Node getTopLevelExpression(Node node) {
        while (node != null && !isTopLevelExpression(node)) {
            if (node instanceof IfStatement ifStatement)
                node = ifStatement.getCondition();
            else if (node instanceof ForStatement forStatement)
                node = forStatement.getCondition();
            else if (node instanceof WhileStatement whileStatement)
                node = whileStatement.getCondition();
            else if (node instanceof DoStatement doStatement)
                node = doStatement.getCondition();
            else
                node = parentMap.getOrDefault(node, null);
        }

        return node;
    }
}
