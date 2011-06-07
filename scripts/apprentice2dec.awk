# convert deck magic-league.com's apprentice format to magarena format

BEGIN {
    first = 0
}

# skip header
/^\/\// {
    gsub("//", "# ")
    print $0
    next
}

/SB:/ && first == 0 {
    print "# Sideboard"
    first = 1
}

/SB:/ {
    gsub("[ ]*SB:[ ]*", "")
    print $0
    next
}

{
    gsub("^[ ]*", "")
    print $0
}

