package org.example.schemes;

import de.fraunhofer.aisec.cpg.graph.Node;
import kotlin.Pair;

import java.util.List;

public record CPG(String functionName,
                  String fileName,
                  List<Node> statements,
                  List<Pair<Integer, Integer>> cfgEdges,
                  List<Pair<Integer, Integer>> cdgEdges,
                  List<Pair<Integer, Integer>> dfgEdges
                  ) {
}
