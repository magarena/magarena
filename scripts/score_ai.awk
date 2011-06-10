#release/decks/LSK_RW.dec        RND     release/decks/LSK_RW.dec        VEGAS   6       10      0       10
#release/decks/LSK_RW.dec        RND     release/decks/LSK_RW.dec        VEGAS   6       10      3       7

BEGIN {
    FS = "\t"
    OFS = "\t"
    s1 = 0
    s2 = 0
}

$2 == ai {
    s1 += $7
    s2 += $8
}

END {
    print s1, s2, s1+s2
    print ai, s1*100/(s1+s2)
}

