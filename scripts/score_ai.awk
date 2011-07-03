#!/usr/bin/awk -f

#release/decks/LSK_RW.dec        RND     6	release/decks/LSK_RW.dec        VEGAS   6       10      0       10
#release/decks/LSK_RW.dec        RND     6	release/decks/LSK_RW.dec        VEGAS   6       10      3       7

BEGIN {
    FS = "\t"
    OFS = "\t"
}

NF == 9 {
    if ($8 + $9 > win[$2] + lose[$2]) {
        s1[$2] -= win[$2]
        s2[$2] -= lose[$2]
    }
    win[$2] = $8
    lose[$2] = $9
    s1[$2] += win[$2]
    s2[$2] += lose[$2]
}

END {
    for (ai in s1) {
        total = s1[ai] + s2[ai]
        print "Total = " total
        print ai, s1[ai], s2[ai], s1[ai]*100/total
    }
}

