package org.example.visitors;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.processing.IVisitor;
import org.example.utils.ParentUtil;
import org.jetbrains.annotations.NotNull;

public class TraversingVisitor extends IVisitor<Node> {
    public ParentUtil parentUtil;

    public TraversingVisitor() {
        this.parentUtil = new ParentUtil();
    }

    @Override
    public void visit(@NotNull Node t) {
        for (Node child: t.getAstChildren())
            this.parentUtil.addParentRelation(child, t);
    }
}
