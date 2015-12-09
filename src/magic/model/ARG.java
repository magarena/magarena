package magic.model;

import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.event.MagicEvent;
import magic.data.EnglishToInt;

import java.util.regex.Matcher;
import java.util.List;
import java.util.Collections;

public class ARG {
    public static final String NUMBER = "(?<number>[0-9]+)";
    public static int number(final Matcher m) {
        return Integer.parseInt(m.group("number"));
    }

    public static final String AMOUNT = "(?<amount>[^ ]+)";
    public static int amount(final Matcher m) {
        return EnglishToInt.convert(m.group("amount"));
    }

    public static final String COST = "(?<cost>.+)";
    public static String cost(final Matcher m) {
        return m.group("cost");
    }

    public static final String COLOR = "(?<color>[^ ]+)";
    public static String color(final Matcher m) {return m.group("color");}

    public static final String EFFECT = "(?<effect>.+)";
    public static String effect(final Matcher m) {
        return m.group("effect");
    }

    public static final String ANY = "(?<any>.+)";
    public static String any(final Matcher m) {
        return m.group("any");
    }

    public static final String MANA = "(?<mana>[^\\.]+)";
    public static String mana(final Matcher m) {
        return m.group("mana");
    }

    public static final String MANACOST = "(?<manacost>(\\{[A-Z\\d/]+\\})+)";
    public static String manacost(final Matcher m) {
        return m.group("manacost");
    }

    public static final String WORD1 = "(?<word1>[^ ]+)";
    public static String word1(final Matcher m) {
        return m.group("word1");
    }

    public static final String WORD2 = "(?<word2>[^ ]+)";
    public static String word2(final Matcher m) {
        return m.group("word2");
    }

    public static final String WORDRUN = "(?<wordrun>[^\\.\"]+?)";
    public static String wordrun(final Matcher m) {
        return m.group("wordrun");
    }

    public static final String WORDRUN2 = "(?<wordrun2>[^\\.\"]+?)";
    public static String wordrun2(final Matcher m) {
        return m.group("wordrun2");
    }

    public static final String COND = "(?<cond>[^,\\.\"]+)";
    public static String cond(final Matcher m) {
        return m.group("cond");
    }

    public static final String PT = "(?<pt>[+-][0-9]+/[+-][0-9]+)";
    public static int[] pt(final Matcher m) {
        final String[] pt = m.group("pt").replace("+","").split("/");
        return new int[]{Integer.parseInt(pt[0]), Integer.parseInt(pt[1])};
    }
    public static MagicPowerToughness mpt(final Matcher m) {
        final String[] pt = m.group("pt").replace("+","").split("/");
        return new MagicPowerToughness(Integer.parseInt(pt[0]), Integer.parseInt(pt[1]));
    }

    public static final String IT = "((?<rn>rn)|(?<sn>sn))";
    public static MagicPermanent itPermanent(final MagicEvent event, final Matcher m) {
        if (m.group("rn") != null) {
            return event.getRefPermanent();
        } else {
            return event.getPermanent();
        }
    }
    public static MagicSource itSource(final MagicEvent event, final Matcher m) {
        if (m.group("rn") != null) {
            return event.getRefSource();
        } else {
            return event.getSource();
        }
    }

    public static final String YOU = "((?<rn>(rn))|(?<pn>(pn||you)))";
    public static MagicTarget youTarget(final MagicEvent event, final Matcher m) {
        if (m.group("rn") != null) {
            return event.getRefTarget();
        } else {
            return event.getPlayer();
        }
    }
    public static MagicPlayer youPlayer(final MagicEvent event, final Matcher m) {
        if (m.group("rn") != null) {
            return event.getRefPlayer();
        } else {
            return event.getPlayer();
        }
    }
    
    public static final String PLAYERS = "((?<rn>(rn))|(?<pn>(pn||you))|(?<group>[^\\.]*) )";
    public static List<MagicPlayer> players(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicPlayer> filter) {
        if (m.group("rn") != null) {
            return Collections.singletonList(event.getRefPlayer());
        } else if (m.group("pn") != null) {
            return Collections.singletonList(event.getPlayer());
        } else {
            return filter.filter(event);
        }
    }

    public static MagicTargetFilter<MagicPlayer> playersParse(final Matcher m) {
        if (m.group("group") != null) {
            return MagicTargetFilterFactory.Player(m.group("group"));
        } else {
            return MagicTargetFilterFactory.PLAYER;
        }
    }

    public static final String PERMANENTS = "((?<rn>rn)|(?<sn>sn)|(?<group>[^\\.]*))";
    public static List<MagicPermanent> permanents(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicPermanent> filter) {
        if (m.group("rn") != null) {
            return Collections.singletonList(event.getRefPermanent());
        } else if (m.group("sn") != null) {
            return Collections.singletonList(event.getPermanent());
        } else {
            return filter.filter(event);
        }
    }
    
    public static MagicTargetFilter<MagicPermanent> permanentsParse(final Matcher m) {
        if (m.group("group") != null) {
            return MagicTargetFilterFactory.Permanent(m.group("group"));
        } else {
            return MagicTargetFilterFactory.ANY;
        }
    }

    public static final String COLON = "\\s*:\\s*";

    public static final String CHOICE = "(?<choice>(a|an|another|target) [^\\.]+?)";
    public static final String CARD   = "(?<choice>[^\\.]* card [^\\.]+?)";
    public static final String GRAVEYARD = "(?<choice>[^\\.]* card [^\\.]+? graveyard)";
}
