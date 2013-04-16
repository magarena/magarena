BEGIN {
    FS = "\t"
}

/index=0/ {
    name = "UNKNOWN"
    if ($0 ~ /MMAB/) {
        name = "MMAB"
    } else if ($0 ~ /VEGAS/) {
        name = "VEGAS"
    } else if ($0 ~ /MCTS/) {
        name = "MCTS"
    }

    cheat = "?"
    if ($0 ~ /cheat=true/) {
        cheat = "C"
    } else if ($0 ~ /cheat=false/) {
        cheat = "H"
    }

    ai1 = name "-" cheat
}

/index=1/ {
    name = "UNKNOWN"
    if ($0 ~ /MMAB/) {
        name = "MMAB"
    } else if ($0 ~ /VEGAS/) {
        name = "VEGAS"
    } else if ($0 ~ /MCTS/) {
        name = "MCTS"
    }

    cheat = "?"
    if ($0 ~ /cheat=true/) {
        cheat = "C"
    } else if ($0 ~ /cheat=false/) {
        cheat = "H"
    }
    
    ai2 = name "-" cheat
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
