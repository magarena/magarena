BEGIN {
    FS = "="
    for (i = 0; i <= 255; i++) {
        ord[sprintf("%c", i)] = i
    }
}

function escape(str, c, len, res) {
    len = length(str)
    res = ""
    for (i = 1; i <= len; i++) {
    c = substr(str, i, 1);
    if (c ~ /[0-9A-Za-z]/)
        res = res c
    else
        res = res "%" sprintf("%02X", ord[c])
    }
    return res
}

/^name=/ {
    name = $2
    url = "http://magiccards.info/query?q=%21" escape(tolower(name))
}

/^url=/ {
    print "url=" url
    next
}

{
    print $0
}
