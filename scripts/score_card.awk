#!/usr/bin/awk -f

# read a list of decks in chronological order and produce a score for each card that appears

# skip comments
/^#/ {
    next
    prev = ""
}

/^\/\// {
    next
    prev = ""
}

{
    # decay of scores
    if (FILENAME != prev) {
        for (i in score) {
            score[i] = 0.95 * score[i]
        }
        prev = FILENAME
    }

    # to normalize mwDeck format
    gsub("\\[.*\\]", "")
    gsub("SB:", "")
    gsub("^ *", "")
    gsub(" +", " ")

    s = gensub("^([0-9]*) ", "\\1|", "g", $0)
    #print s
    split(s, tok, "|")
    #print "|"tok[1]"|"
    cnt = tok[1]
    gsub("|","",tok[2])
    #print "|"tok[2]"|"
    name = tok[2]

    score[name] += cnt
}

END {
    for (i in score) {
        print score[i] "\t" i
    }
}

