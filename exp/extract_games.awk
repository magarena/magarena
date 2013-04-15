BEGIN {
    FS = "\t"
}

/Saved Player/ {
    getline
    getline

    if ($8 == "0") {
        C = "W"
    } else {
        C = "B"
    }
    print ai1 " " ai2 " " C
}
