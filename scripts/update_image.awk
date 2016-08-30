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
    print "image=" image
    next
}

NF == 1 {
    print $0
}
