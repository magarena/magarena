package magic.model;

public enum MagicIdentifierType {
    Permanent,
    PermanentTrigger,
    ItemOnStack;

    public static final int NR_OF_IDENTIFIERS=values().length;
}
