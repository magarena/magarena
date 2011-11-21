BEGIN {
    FS = "\t"
}

FILENAME ~ /existing/ {
    impl[$1] = 0
    next
}

{
    name = $1
    if (name in impl) {
        found[name] = 1
        print name
        while ($0 != "") {
            getline
			gsub(name,"@")
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
