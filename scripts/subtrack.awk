BEGIN {
    FS = "\t"
}

NF == 1 {
    imp[$0] = 1
}

NF == 2 {
    if (!($2 in imp)) {
        print $0 "\t" "comment"
    }
}
