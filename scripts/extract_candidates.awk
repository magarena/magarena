BEGIN {
    FS = "\t"
    OFS = "\t"
    ORS = ""
}

FILENAME ~ /data/ {
    if ($1 in score) {
        print score[$1] "\t"
        print "NOTE:"comment[$1] "\t"
        print "NAME:"$1 "\t"
        print "TEXT:" "\t"
        while ($0 != "") {
            getline
            print $0 "\t"
        }
        cnt++
        print "\n"
    }
    next
}

NF == 1 {
    score[$1] = 1
}

NF >= 2 {
    score[$2] = $1
    comment[$2] = $3
    next
}

END {
    print "found " cnt " card" > "/dev/stderr"
}
