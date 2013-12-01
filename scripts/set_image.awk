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
    image = "http://mtgimage.com/card/" escape(tolower(name)) ".jpg"
}

/^image=/ {
    print "image=" image
    next
}

{
    print $0
}
