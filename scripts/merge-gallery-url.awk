BEGIN {
    FS = "\t"
}

NF == 2 {
    url[$1] = $2
    #print $1 $2
    next
}

/#image/ {
    print "image=" url[name]
    next
}

{
    print
}

/name=/ {
    gsub("name=","")
    name = $0
}
