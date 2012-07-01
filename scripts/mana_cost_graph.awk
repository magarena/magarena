BEGIN {
    FS = "[{}]"
    print "strict digraph G {"
}

{
    first = 0
    for (i = 1; i <= NF; i++) {
        if ($i != "") {
            if (!first) {
                printf("\"%s\"", $i);
                first = 1;
            } else { 
                printf(" -> \"%s\"", $i);
            }
        }
    }
    printf("\n");
}


END {
    print "}"
}
