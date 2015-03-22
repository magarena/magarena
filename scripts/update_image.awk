# input: 
#   mapping file with one <card name><tab><image url> per line
#   card script file to be updated, with image=<set>
# output:
#   updated card script content
# condition:
#   only update if computed image url has the same set

BEGIN {
    FS = "\t"
}

NF == 2 {
    map[$1] = $2
}

/^name=/ {
    name = gensub("name=","","g", $0)
    image = map[name]
}

/^image=/ {
    old_set = gensub("image=","","g", $0)
    new_set = gensub("http://magiccards.info/scans/en/", "", "g", image)
    new_set = gensub("/[^/]*.jpg", "", "g", new_set)
    if (old_set == new_set) {
        print "image=" image
    } else {
        print $0
    }
    next
}

NF == 1 {
    print $0
}
