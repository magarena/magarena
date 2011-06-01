BEGIN {
    FS = "\t"
    OFS = "\t"
}

{
    if ($3 != "comment") {
        comment[$2] = $3
    }
}

FILENAME ~ /candidate/ {
    if ($2 in comment) {
        $3 = comment[$2]
    }
    print $1,$2,$3
}
