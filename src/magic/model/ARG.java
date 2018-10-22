package magic.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import magic.data.EnglishToInt;
import magic.data.RomanToInt;
import magic.model.action.MagicPlayMod;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

public class ARG {
    public static final String COLON = "\\s*:\\s*";

    public static final String CHOICE = "(?<choice>(a|an|another|target) [^\\.]+?)";
    public static final String TARGET_CONTROLS = "(?<tpgroup>[^\\.]* (?<tpchoice>target (player|opponent)) controls)";
    public static final String CARD   = "(?<choice>[^\\.]* card [^\\.]+?)";
    public static final String THING = "(permanent|creature|artifact|land|spell or ability|spell|ability)";
    public static final String PLAYER = "(player|opponent)";
    public static final String EVENQUOTES = "(?=([^\"]*'[^\"]*')*[^\"]*$)";
    public static final String CARDTYPE   = "(?<cardtype>(creature|artifact|land|enchantment|conspiracy|instant|phenomenon|plane|planeswalker|scheme|sorcery|tribal|vanguard))";

    public static final String ENERGY = "(?<energy>(\\{E\\})+)";
    public static int energy(final Matcher m) {
        return m.group("energy").length() / 3;
    }

    public static final String NUMBER = "(?<number>[0-9]+)";
    public static int number(final Matcher m) {
        if (m.group("number") == null) {
            return 1;
        } else {
            return Integer.parseInt(m.group("number"));
        }
    }

    public static final String NUMBER2 = "(?<number2>[0-9]+)";
    public static int number2(final Matcher m) {
        if (m.group("number2") == null) {
            return 1;
        } else {
            return Integer.parseInt(m.group("number2"));
        }
    }

    public static final String AMOUNT = "(?<amount>(a|an|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve|thirteen|fourteen|fifteen|twenty|x|[0-9]+))";
    public static int amount(final Matcher m) {
        return EnglishToInt.convert(m.group("amount"));
    }
    public static MagicAmount amountObj(final Matcher m) {
        return MagicAmountParser.build(m.group("amount"));
    }

    public static MagicType cardType(final Matcher m) {
        return MagicType.getType(m.group("cardtype"));
    }

    public static final String AMOUNT2 = "(?<amount2>[^ ]+?)";
    public static int amount2(final Matcher m) {
        return EnglishToInt.convert(m.group("amount2"));
    }
    public static MagicAmount amount2Obj(final Matcher m) {
        return MagicAmountParser.build(m.group("amount2"));
    }

    public static final String EACH = "( |, )?((for each|equal to|where X is) " + ARG.WORDRUN + ")?";
    public static MagicAmount each(final Matcher m) {
        return MagicAmountParser.build(ARG.wordrun(m));
    }

    public static final String COST = "(?<cost>.+)";
    public static final String MAY_COST = "(?<cost>(?!search|draw|exile).+)";
    public static String cost(final Matcher m) {
        return m.group("cost");
    }

    public static final String COLOR = "(?<color>[^ ]+)";
    public static MagicColor color(final Matcher m) {
        return MagicColor.getColor(m.group("color"));
    }

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
    public static final String XPT = "(?<pt>[+-][X0]+/[+-][X0]+)";
    public static int[] pt(final Matcher m) {
        final String[] pt = m.group("pt").replace("+","").split("/");
        return new int[]{Integer.parseInt(pt[0]), Integer.parseInt(pt[1])};
    }
    public static String[] ptStr(final Matcher m) {
        return m.group("pt").replace("+","").split("/");
    }
    public static MagicPowerToughness mpt(final Matcher m) {
        final String[] pt = m.group("pt").replace("+","").split("/");
        return new MagicPowerToughness(Integer.parseInt(pt[0]), Integer.parseInt(pt[1]));
    }

    public static final String MODS = "(\\.|,)?( )?(?<mods>.+?)??";
    public static List<MagicPlayMod> mods(final Matcher m) {
        return MagicPlayMod.build(m.group("mods"));
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

    private static final String TNC = "(that [^ ]+'s|its) controller( or that player)?";
    public static final String PLAYERS = "((?<rnc>rn's controller)|(?<tnc>" + TNC + ")|(?<rn>rn)|(?<pn>(pn||you))|" + CHOICE + "|(?<group>[^\\.]+?))";
    public static List<MagicPlayer> players(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicPlayer> filter) {
        if (m.group("rnc") != null) {
            return Collections.singletonList(event.getRefObject().getController());
        } else if (m.group("tnc") != null) {
            return event.listTargetController();
        } else if (m.group("rn") != null) {
            return Collections.singletonList(event.getRefPlayer());
        } else if (m.group("pn") != null) {
            return Collections.singletonList(event.getPlayer());
        } else if (m.group("choice") != null) {
            return event.listTargetPlayer();
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

    public static final String PERMANENTS = "((?<rn>rn)|(?<sn>sn)|" + CHOICE + "|" + TARGET_CONTROLS + "|(?<group>[^\\.]+?))";
    public static List<MagicPermanent> permanents(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicPermanent> filter) {
        if (m.group("rn") != null) {
            return Collections.singletonList(event.getRefPermanent());
        } else if (m.group("sn") != null) {
            return Collections.singletonList(event.getPermanent());
        } else if (m.group("choice") != null) {
            return event.listTargetPermanent();
        } else if (m.group("tpchoice") != null) {
            return filter.filter(event.getSource(), event.listTargetPlayer().get(0), MagicTargetHint.None);
        } else {
            return filter.filter(event);
        }
    }

    public static MagicTargetFilter<MagicPermanent> permanentsParse(final Matcher m) {
        if (m.group("group") != null) {
            return MagicTargetFilterFactory.Permanent(m.group("group"));
        } else if (m.group("tpgroup") != null) {
            return MagicTargetFilterFactory.Permanent(m.group("tpgroup").replaceFirst("target (player|opponent) controls", "you control"));
        } else {
            return MagicTargetFilterFactory.ANY;
        }
    }

    public static final String TARGETS = "((?<rnc1>rn's controller)|(?<tnc1>" + TNC + ")|(?<rn1>rn)|(?<sn1>sn)|(?<pn1>(pn||you))|" + CHOICE + "|(?<group1>[^\\.]+?))";
    public static List<? extends MagicTarget> targets(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicTarget> filter) {
        if (m.group("rnc1") != null) {
            return Collections.singletonList(event.getRefObject().getController());
        } else if (m.group("tnc1") != null) {
            return event.listTargetController();
        } else if (m.group("rn1") != null) {
            return Collections.singletonList(event.getRefTarget());
        } else if (m.group("pn1") != null) {
            return Collections.singletonList(event.getPlayer());
        } else if (m.group("sn1") != null) {
            return Collections.singletonList(event.getPermanent());
        } else if (m.group("choice") != null) {
            return event.listTarget();
        } else {
            return filter.filter(event);
        }
    }

    public static MagicTargetFilter<MagicTarget> targetsParse(final Matcher m) {
        if (m.group("group1") != null) {
            return MagicTargetFilterFactory.Target(m.group("group1"));
        } else {
            return MagicTargetFilterFactory.ONE;
        }
    }

    public static final String TARGETS2 = "((?<tnc2>" + TNC + ")|(?<rn2>rn)|(?<sn2>sn)|(?<pn2>(pn||you))|(?<group2>[^\\.]+?))";
    public static List<? extends MagicTarget> targets2(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicTarget> filter) {
        if (m.group("tnc2") != null) {
            return event.listTargetController();
        } else if (m.group("rn2") != null) {
            return Collections.singletonList(event.getRefTarget());
        } else if (m.group("pn2") != null) {
            return Collections.singletonList(event.getPlayer());
        } else if (m.group("sn2") != null) {
            return Collections.singletonList(event.getPermanent());
        } else {
            return filter.filter(event);
        }
    }

    public static MagicTargetFilter<MagicTarget> targets2Parse(final Matcher m) {
        if (m.group("group2") != null) {
            return MagicTargetFilterFactory.Target(m.group("group2"));
        } else {
            return MagicTargetFilterFactory.ONE;
        }
    }

    public static final String GRAVEYARD_CARDS = "((?<choice>[^\\.]* card [^\\.]+? graveyard)|(?<group>[^\\.]* cards [^\\.]+? graveyard))";
    public static final String CARDS = "((?<choice>[^\\.]* card [^\\.]+?)|(?<group>[^\\.]* cards [^\\.]+?))";
    public static String cards(final Matcher m) {
        if (m.group("choice") != null) {
            return m.group("choice");
        } else {
            return m.group("group");
        }
    }

    public static List<MagicCard> cards(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicCard> filter) {
        if (m.group("choice") != null) {
            return event.listTargetCard();
        } else {
            return filter.filter(event);
        }
    }

    public static MagicTargetFilter<MagicCard> cardsParse(final Matcher m) {
        if (m.group("group") != null) {
            return MagicTargetFilterFactory.Card(m.group("group"));
        } else {
            return MagicTargetFilterFactory.CARD_FROM_HAND;
        }
    }

    public static final String ITEMS = "((?<rn>rn)|(?<sn>sn)|" + CHOICE + "|(?<group>[^\\.]+?))";
    public static List<MagicItemOnStack> items(final MagicEvent event, final Matcher m, final MagicTargetFilter<MagicItemOnStack> filter) {
        if (m.group("rn") != null) {
            return Collections.singletonList(event.getRefItemOnStack());
        } else if (m.group("sn") != null) {
            return Collections.singletonList(event.getItemOnStack());
        } else if (m.group("choice") != null) {
            return event.listTargetItem();
        } else {
            return filter.filter(event);
        }
    }

    public static MagicTargetFilter<MagicItemOnStack> itemsParse(final Matcher m) {
        if (m.group("group") != null) {
            return MagicTargetFilterFactory.ItemOnStack(m.group("group"));
        } else {
            return MagicTargetFilterFactory.SPELL_OR_ABILITY;
        }
    }

    public static final String CHAPTERS = "(?<chapters>I+(, I+)*)";
    public static int[] chapters(final Matcher m) {
        final String[] chapters = m.group("chapters").split(", ");
        return Arrays.stream(chapters).mapToInt(RomanToInt::convert).toArray();
    }

}
