BEGIN {
    FS = "\t"
}

!(FILENAME ~ /mtg-data/) {
    impl[$1] = 0
    next
}

FILENAME ~ /mtg-data/ && $0 == "" {
    getline
    name = $1
    if (name in impl) {
        found[name] = 1
        print ""
    }
}

name in found {
    print
}

END {
    print "found " length(found) " cards" > "/dev/stderr"
    for (i in impl) {
        if (!(i in found)) {
            print i " not found" > "/dev/stderr"
        }
    }
}
