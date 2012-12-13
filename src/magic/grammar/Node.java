package magic.grammar;

import java.util.List;
import java.util.LinkedList;

public class Node {
    public String rule;
    public String text = "";

    private List<Node> children = new LinkedList<Node>();

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(rule);
        sb.append('(');
        sb.append(text);
        for (final Node n : children) {
            sb.append(n.toString());
        }
        sb.append(')');
        return sb.toString();
    }

    public void add(final Node n) {
        children.add(n);
    }

    public void clear() {
        children.clear();
    }
}
