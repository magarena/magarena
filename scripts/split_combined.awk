# src/magic/data/CardEventDefinitions.java
# src/magic/data/PermanentActivationDefinitions.java
# src/magic/data/TriggerDefinitions.java
# generate src/magic/card/Card_Name.java based on the above

BEGIN {
    FS = "\""
}
	
/private static/ {
    card = $2
    fn = gensub("[^A-Za-z]", "_", "g", card);
    public = gensub("private","public","g")
    clean = gensub(" [A-Za-z0-9_]*[ ]*=", " V" NR " =", "g", public)
    block[fn] = block[fn] "\n" clean
    next
}

{
    block[fn] = block[fn] "\n" $0
}

END {
    header ="package magic.card;\n" \
            "import java.util.*;\n" \
            "import magic.model.event.*;\n" \
            "import magic.model.stack.*;\n" \
            "import magic.model.choice.*;\n" \
            "import magic.model.target.*;\n" \
            "import magic.model.action.*;\n" \
            "import magic.model.trigger.*;\n" \
            "import magic.model.condition.*;\n" \
            "import magic.model.*;\n" \
            "import magic.data.*;\n" \
            "import magic.model.variable.*;\n"
    for (fn in block) {
        output = fn ".java"
        print header > output 
        print "public class " fn " {" > output
        print block[fn] > output
        print "}" > output
    }
    print length(block) > "/dev/stderr"
}
