package org.example.schemes;

import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.sarif.Region;

import java.util.Objects;

public record Edge(Node start, Node end) implements Comparable<Edge> {
    @Override
    public int compareTo(Edge other) {
        Region thisStartRegion = this.start().getLocation().getRegion();
        Region thisEndRegion = this.end().getLocation().getRegion();
        Region otherStartRegion = other.start().getLocation().getRegion();
        Region otherEndRegion = other.end().getLocation().getRegion();
        int start_line_compare = Integer.compare(thisStartRegion.startLine, otherStartRegion.startLine);
        if (start_line_compare != 0)
            return start_line_compare;

        int start_col_compare = Integer.compare(thisStartRegion.startColumn, otherStartRegion.startColumn);
        if (start_col_compare != 0)
            return start_col_compare;

        int end_line_compare = Integer.compare(thisEndRegion.startLine, otherEndRegion.startLine);
        if (end_line_compare != 0)
            return end_line_compare;

        return Integer.compare(thisEndRegion.startLine, otherEndRegion.startColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return this.start() == edge.start && this.end() == edge.end;
    }
}
