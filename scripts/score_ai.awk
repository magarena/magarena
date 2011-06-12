#release/decks/LSK_RW.dec        RND     release/decks/LSK_RW.dec        VEGAS   6       10      0       10
#release/decks/LSK_RW.dec        RND     release/decks/LSK_RW.dec        VEGAS   6       10      3       7

BEGIN {
    FS = "\t"
    OFS = "\t"
    s1 = 0
    s2 = 0
}

$2 == ai {
    if ($7 + $8 > win + lose) {
        s1 -= win
        s2 -= lose
    }
    win = $7
    lose = $8
    s1 += win
    s2 += lose
}

END {
    print s1, s2, s1+s2
    print ai, s1*100/(s1+s2)
}

