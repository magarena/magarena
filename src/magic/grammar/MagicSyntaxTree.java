package magic.grammar;

class MagicSyntaxTree extends magic.grammar.SemanticsBase {
    Node tree;
    boolean action() {
        final Node node = new Node();
        node.rule = lhs().rule();

        int numTerm = 0;
        int numRule = 0;

        for (int i = 0; i < rhsSize(); i++) {
            if (rhs(i).get() != null && rhs(i).get() instanceof Node) {
                node.add((Node)rhs(i).get());
                numRule++;
            } else if (rhs(i).isTerm()) {
                final Node child = new Node();
                child.rule = "T";
                child.text = rhs(i).text();
                node.add(child);
                numTerm++;
            }
        }

        if (numRule == 0 ||
            lhs().rule().startsWith("Select") ||
            lhs().rule().endsWith("Number") ||
            lhs().rule().endsWith("Count") ||
            lhs().rule().endsWith("Duration") ||
            lhs().rule().endsWith("Keyword") ||
            lhs().rule().equals("ManaCost")) {
            node.clear();
            node.text = lhs().text();
        }

        if ("Rule".equals(lhs().rule())) {
            tree = node;
        } else if ("SPACE".equals(lhs().rule()) ||
                   "EOS".equals(lhs().rule()) ||
                   "EOR".equals(lhs().rule())) {
            //do nothing
        } else {
            lhs().put(node);
        }
        return true;
    }
}
