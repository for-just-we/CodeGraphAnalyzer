package org.example.visitors;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.processing.IVisitor;

import java.util.ArrayList;
import java.util.List;

public class ASTNodeCollector extends IVisitor<Node> {
    public List<Node> nodes;

    public ASTNodeCollector() {
        nodes = new ArrayList<>();
    }

    @Override
    public void visit(Node t) {
        if (!nodes.contains(t))
            nodes.add(t);
    }
}
