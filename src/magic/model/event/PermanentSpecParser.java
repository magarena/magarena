package magic.model.event;

import magic.model.MagicColor;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.MagicAbility;
import magic.model.MagicAbilityList;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;

public class PermanentSpecParser {
    public final int[] pt;
    public final Set<MagicColor> colors;
    public final Set<MagicSubType > subTypes;
    public final Set<MagicType> types;
    public final MagicAbilityList abilities;
    public final boolean duration;
    public final boolean additionTo;

    public static final String BECOMES = " become(s)?( a| an)?(?<legendary> legendary)?( )?(?<pt>[0-9]+/[0-9]+)? (?<all>.*?)( with base power and toughness (?<pt2>[0-9]+/[0-9]+))?( (with|and gains) (?<ability>.*?))?";

    public PermanentSpecParser(final Matcher matcher) {
        final String ptStr = matcher.group("pt") != null ? matcher.group("pt") :
                             matcher.group("pt2") != null ? matcher.group("pt2") :
                             null;
        final String[] ptTok = ptStr == null ? null : ptStr.split("/");
        pt = ptTok == null ? null : new int[]{Integer.parseInt(ptTok[0]), Integer.parseInt(ptTok[1])};

        final List<String> tokens = new LinkedList<>(Arrays.asList(matcher.group("all").split(", | and | ")));
        colors = MagicColor.prefixColors(tokens);
        subTypes = MagicSubType.prefixSubTypes(tokens);
        types = MagicType.prefixTypes(tokens);
        if (matcher.group("legendary") != null) {
            types.add(MagicType.Legendary);
        }

        if (tokens.isEmpty() == false) {
            throw new RuntimeException("unmatched becomes specification " + tokens);
        }

        abilities = matcher.group("ability") == null ? null :
            MagicAbility.getAbilityList(matcher.group("ability"));

        duration = matcher.group("duration") != null;
        additionTo = matcher.group("additionTo") != null;
    }
}
