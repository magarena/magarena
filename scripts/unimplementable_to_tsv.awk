/# / {
    reason = gensub("^# ", "", "g")
    next
}

/[A-Z]/ {
    print $0 "\t" reason
}
