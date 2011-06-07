function norm(name) {
    return gensub("[^a-z]", "", "g", tolower(name))
}

BEGIN {
    FS = "\t"
    total = 0
    inmag = 0
    result = ""
}

FILENAME ~ /magarena/ {
    cards[norm($0)] = $0
    #print norm($0)
    #print "|"$0"|" " in Magarena"
    next
}

/Sideboard/ {
    result = result "## " $0 " ##\n"
}

# card in deck (excluding the line containing sideboard)
/\w+ \w+/ {
    s = index($0, " ")
    n = substr($0, 1, s - 1)
    card = substr($0, s + 1)
    id = norm(card)
    #print "|" card "|"
    total += n
    if (id in cards) {
        inmag += n
        result = result n " " cards[id] "\n"
    } else {
        result = result "# " n " " card "\n"
    }
}

END {
    print FILENAME "\t" inmag "/" total "\t" (inmag/total) > "/dev/stderr"
    if (inmag >= 40) {
        print result
    }
}
