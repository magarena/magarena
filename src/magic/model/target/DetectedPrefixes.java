package magic.model.target;

import magic.model.MagicColor;
import magic.model.MagicSubType;
import magic.model.MagicType;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsed restriction on color, type and subtype.
 */
public class DetectedPrefixes {
    /**
     * Type of detected prefix. If not None, then one of the other fields will be set.
     */
    public PrefixType prefixType;
    public MagicColor color;
    public MagicType type;
    public MagicSubType subType;

    private DetectedPrefixes(PrefixType prefixType, MagicColor color, MagicType type, MagicSubType subType) {
        this.prefixType = prefixType;
        this.color = color;
        this.type = type;
        this.subType = subType;
    }

    private static final Pattern COLOR_REGEX = makeGroupRegex(MagicColor.values());
    private static final Pattern TYPE_REGEX = makeGroupRegex(MagicType.values());
    private static final Pattern SUBTYPE_REGEX = makeGroupRegex(MagicSubType.values());

    /**
     * Make a regex that matches if text matches one of enum's values, optionally prefixed with "non" or "non-"
     */
    private static Pattern makeGroupRegex(Enum[] values) {
        StringJoiner joiner = new StringJoiner("|");
        for (Enum i : values) {
            joiner.add(i.toString());
        }
        String re = "(non-?)?(" + joiner.toString() + ")";
        return Pattern.compile(re, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Parse prefix alone, returning recognized restriction, if any.
     */
    public static DetectedPrefixes parseFrom(String prefix) {
        // Example: black / nonblack
        Matcher m1 = COLOR_REGEX.matcher(prefix);
        if (m1.matches()) {
            boolean isNegative = m1.group(1) != null;
            String base = m1.group(2);
            for (final MagicColor c : MagicColor.values()) {
                if (base.equalsIgnoreCase(c.getName())) {
                    return new DetectedPrefixes(isNegative ? PrefixType.NonColor : PrefixType.Color,
                            c, null, null);
                }
            }
        }
        // Example: land / nonland
        Matcher m2 = TYPE_REGEX.matcher(prefix);
        if (m2.matches()) {
            boolean isNegative = m2.group(1) != null;
            String base = m2.group(2);
            for (final MagicType t : MagicType.values()) {
                if (base.equalsIgnoreCase(t.toString())) {
                    return new DetectedPrefixes(isNegative ? PrefixType.NonType : PrefixType.Type,
                            null, t, null);
                }
            }
        }
        // Example: Vampire / non-Vampire
        Matcher m3 = SUBTYPE_REGEX.matcher(prefix);
        if (m3.matches()) {
            boolean isNegative = m3.group(1) != null;
            String base = m3.group(2);
            for (final MagicSubType st : MagicSubType.values()) {
                if (base.equalsIgnoreCase(st.toString())) {
                    return new DetectedPrefixes(isNegative ? PrefixType.NonSubType : PrefixType.SubType,
                            null, null, st);
                }
            }
        }
        // Nothing detected
        return new DetectedPrefixes(PrefixType.None, null, null, null);
    }

    enum PrefixType {
        None, Color, NonColor, Type, NonType, SubType, NonSubType
    }
}
