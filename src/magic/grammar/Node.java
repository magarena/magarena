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

    public void show() {
        show(0);
    }

    public void show(int gap) {
        for (int i = 0; i < gap; i++) {
            System.out.print(" ");
        }
        System.out.print(rule);
        if (!text.isEmpty()) {
            System.out.print(" - " + text);
        }
        System.out.println();
        for (final Node n : children) {
            n.show(gap + 4);
        }
    }

    public void add(final Node n) {
        children.add(n);
    }

    public void clear() {
        children.clear();
    }
}
