# input: card scripts
# output: html file with name and image

BEGIN {
    FS = "="
}

/name=/ {
    print "<p>" $2 "</p>"
}

/image=/ {
    print "<img src=\"" $2 "\"/>"
}
