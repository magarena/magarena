BEGIN {
    FS = "\t"
}

FILENAME ~ /existing/ {
    impl[$1] = 0
    next
}

{
    if ($1 in impl) {
        found[$1] = 1
        print $1
        while ($0 != "") {
            getline
            print $0
        }
        cnt++
    }
}

END {
    print "found " cnt " card" > "/dev/stderr"
    for (i in impl) {
        if (!(i in found)) {
            print i " not found" > "/dev/stderr"
        }
    }
}
