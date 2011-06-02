BEGIN {
    FS = "\t"
    OFS = "\t"
}

# list of existing cards
NF == 1 {
    imp[$0] = 1
}

# score, card, comments
NF == 3 {
    comment[$2] = $3
}

# new scores, card
NF == 2 {
    if ($2 in comment) {
        print $0, comment[$2]
    } else if ($2 in imp) {
        print $0, "implemented"
    } else {
        print $0, "comment"
    }
}

