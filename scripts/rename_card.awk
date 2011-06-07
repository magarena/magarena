BEGIN {
    FS = "="
}

$1 == "primitive" {
    cname = $2
}

$1 == "id" {
    cid = $2
    print "mv \"" cname ".jpg\" " cid
}
