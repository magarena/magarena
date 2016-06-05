BEGIN {
    FS = "\t"
    OFS = "\t"
    ORS = ""
}

FILENAME ~ /mtg-data/ {
    if ($1 in score) {
        found[$1] = 1;
        cnt++
    }

    if ($1 in found && !($1 in ignore)) {
        print score[$1] "\t"
        print "NOTE:"comment[$1] "\t"
        print "NAME:"$1 "\t"
        print "TEXT:" "\t"
        while ($0 != "") {
            getline
            print $0 "\t"
        }
        print "\n"
    } else {
        while ($0 != "") {
            getline
        }
    }

    next
}

FILENAME ~ /unimplementable/ {
    ignore[$1] = 1
    next
}

FILENAME ~ /cards\/groovy.txt/ {
    comment[$1] = "groovy"
    next
}

{
    if (NF == 1) {
        score[$1] = 1
    } else if (NF == 2) {
        score[$2] = $1
        comment[$2] = $3
    } else {
        print "unsupported format"
        exit 1
    }
}

END {
    ORS = "\n"
    print "found " cnt " cards" > "/dev/stderr"
    for (i in score) {
        if (!(i in found)) {
            print i " not found" > "/dev/stderr"
        }
    }
}
