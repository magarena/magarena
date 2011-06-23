BEGIN {
    FS = "\t"
}

# list of existing cards
NF == 1 {
    existing[$1] = 1
}

# list of scored cards
NF == 2 && !($2 in existing) {
    print $0
}
