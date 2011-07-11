BEGIN {
    FS = "\t"
    less = 0;
    less_lose = 0;
    greater = 0;
    greater_win = 0;
    total = 0;
    saved = 0;
}

NR % 1000 == 0 {
    print less, less_lose, greater, greater_win, total, saved
}

{
    total += NF - 1
    for (i = 1; i < NF; i++) {
        if ($i < -t) {
            less++
            saved += NF - 1 - i
            if ($NF == "LOSE") {
                less_lose++
            }
            next
        } else if ($i > t) {
            greater++
            saved += NF - 1 - i
            if ($NF == "WIN") {
                greater_win++
            }
            next
        }
    }
}


END {
    print t, (greater_win)/(greater), (less_lose)/(less), saved/total
}
