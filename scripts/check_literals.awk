BEGIN {
    FS = "[_/\".]"
}

{
    for (i = 5; i <= NF; i++) {
        if ($i ~ $4) {
            print $0
            break
        }
    }
}

