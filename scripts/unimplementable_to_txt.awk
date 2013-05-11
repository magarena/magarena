BEGIN {
    FS = "\t"
}

{
    reason[$2] = reason[$2] $1 "\n"
}

END {
    for (i in reason) {
        print "# " i
        print reason[i]
    }
}
