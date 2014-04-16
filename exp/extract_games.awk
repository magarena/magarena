BEGIN {
    FS = "\t"
}

function genName(name) {
    if (name ~ /minimax (cheat)/) {
        return "MMAB-C"
    } else if ($0 ~ /vegas (cheat)/) {
        return "VEGAS-C"
    } else if ($0 ~ /monte carlo tree search (cheat)/) {
        return "MCTS-C"
    } else if ($0 ~ /minimax/) {
        return "MMAB-H"
    } else if ($0 ~ /vegas/) {
        return "VEGAS-H"
    } else if ($0 ~ /monte carlo tree search/) {
        return "MCTS-H"
    } else {
        return "UNKNOWN"
    }
}

/index=0/ {
    ai1 = genName()
}

/index=1/ {
    ai2 = genName()
}

/^\t/ {
    getline
    if ($8 == "0") {
        C = "1"
    } else {
        C = "0"
    }
    print genName($2) "-" $3 " " genName($5) "-" $6  " " C
}
