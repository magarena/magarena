/^MCTS/ {
    show = 1
}

/^\*/ {
    if (show) {
        show = 0
        print $0
    }
}

/lost the game/ {
    print  
    print ""
}
