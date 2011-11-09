/^>/ {
    gsub(">","")
    name = $0
    fn = gensub("[^A-Za-z]", "_", "g", name) ".txt";
    print ">" name >> fn
    next
}

{
    print $0 >> fn
}


