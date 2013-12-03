function leading_space() {
    cnt = 0
    split($0, chars, "")
    for (i = 1; i < length(chars); i++) {
        if (chars[i] ~ " ") {
            cnt++
        } else {
            break;
        }
    }
    return cnt
}

/processTarget.*new Magic.*Action()/ {
    gsub(",new Magic.*Action\\(\\) {", ", {")
    gsub("new Magic.*Action\\(\\) {", "{")
    state = 1
}

# remove terminating }
state == 2 && leading_space() == space - 4 {
    state = 0
    next
}

# remove one level of indentation
state == 2 && leading_space() >= space {
    sub("    ", "");
}

# remove doAction with just param
state == 1 && /doAction/ {
    gsub("public void doAction\\(", "")
    gsub("\\) {", " ->")
    space = leading_space() + 4;
    state = 2
}

{
    print $0
}
