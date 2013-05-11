BEGIN {
    FS = "\t"
}

function genName() {
    name = "UNKNOWN"
    if ($0 ~ /MMAB/) {
        name = "MMAB"
    } else if ($0 ~ /VEGAS/) {
        name = "VEGAS"
    } else if ($0 ~ /MCTS2/) {
        name = "MCTS2"
    } else if ($0 ~ /MCTS/) {
        name = "MCTS"
    }

    cheat = "?"
    if ($0 ~ /cheat=true/) {
        cheat = "C"
    } else if ($0 ~ /cheat=false/) {
        cheat = "H"
    }

    return name "-" cheat
}

/index=0/ {
    ai1 = genName()
}

/index=1/ {
    ai2 = genName()
}

/Saved Player/ {
    getline
    getline

    if ($8 == "0") {
        C = "W"
    } else {
        C = "B"
    }
    print ai1 "-" $3 " " ai2 "-" $6  " " C
}
