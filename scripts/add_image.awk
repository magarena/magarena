/^>/ {
    name = gensub(">","","g", $0)
    query = gensub(" ","%20","g", name)
    query = gensub("'","%27","g", query)
    command = "curl -s http://magiccards.info/query?q=%21" query " | grep info/scan"
    print command > "/dev/stderr"
    url = "QQQ"
    command | getline url
    url = gensub("<[^\"]*\"","","g", url)
    url = gensub("\"","","g", url)
    print $0
    print "image="url
    print url > "/dev/stderr"
    next
}

{
    print $0
}
