# extract deck from Daily MTG's Daily Deck List page

BEGIN {
}

/<h4>/ {
    gsub("<[^>]*>","|")
    split($0, tok, "|")
    name = tok[2]
    print "#", name
}

/<h5 class="byline">/ {
    gsub("<[^>]*>","|")
    split($0, tok, "|")
    date = tok[3]
    print "#", date
}
          
/<heading>/ {
    gsub("<[^>]*>","|")
    split($0, tok, "|")
    heading = tok[2]
    print "#", heading
}

/<div class="sub">/ {
    gsub("<[^>]*>","|")
    split($0, tok, "|")
    subhead = tok[2]
    print "#", subhead
}

/<td valign="top" width="185">/ {
    gsub("<[^>]*>","|")
    split($0, tok, "|")
    cnt = tok[2]
    gsub("[^[:digit:]]", "", cnt)
    #print cnt 
}

# weird format
/^[ ]*<i>Sideboard/ {
    getline
    getline
    print "# Sideboard"
    gsub("<[^>]*>","|")
    split($0, tok, "|")
    cnt = tok[2]
    gsub("[^[:digit:]]", "", cnt)
}


/class="nodec" onmouseover="ChangeBigCard/ {
    gsub("<[^>]*>","|")
    split($0, tok, "|")
    #print $0
    card = tok[2]
    print cnt " " card
    if (tok[12] == "Sideboard") {
        print "# Sideboard"
        cnt = tok[15]
    } else if (tok[4] != "") {
        cnt = tok[4]
    } else {
        cnt = tok[9]
    }
    gsub("[^[:digit:]]", "", cnt)
    #print cnt
}
