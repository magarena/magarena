package magic.model.target;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.model.ARG;
import magic.model.MagicPermanentState;
import magic.model.target.MagicTargetFilterFactory.Control;

public enum MagicTargetFilterParser {

    CardNamedFromYourLibrary("card named " + ARG.ANY + " from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.cardName(ARG.any(arg)).from(MagicTargetType.Library);
        }
    },
    CardNamedFromYourHand("card named " + ARG.ANY + " from your hand") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.cardName(ARG.any(arg)).from(MagicTargetType.Hand);
        }
    },
    CardNamedFromYourGraveyard("card named " + ARG.ANY + " from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.cardName(ARG.any(arg)).from(MagicTargetType.Graveyard);
        }
    },
    CardNamedFromOppGraveyard("card named " + ARG.ANY + " from an opponent's graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.cardName(ARG.any(arg)).from(MagicTargetType.OpponentsGraveyard);
        }
    },
    CardNamedFromGraveyard("card named " + ARG.ANY + " from a graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.cardName(ARG.any(arg)).from(MagicTargetType.Graveyard).from(MagicTargetType.OpponentsGraveyard);
        }
    },
    PermanentNamed("permanent named " + ARG.ANY) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.permanentName(ARG.any(arg), Control.Any);
        }
    },
    PermanentNotNamed("permanent not named " + ARG.ANY) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.permanentNotName(ARG.any(arg), Control.Any);
        }
    },
    CreatureNamedYouControl("creature named " + ARG.ANY + " you control") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.creatureName(ARG.any(arg), Control.You);
        }
    },
    CreatureYouControlNamed("creature you control named " + ARG.ANY) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.creatureName(ARG.any(arg), Control.You);
        }
    },
    CreatureNamed("creature named " + ARG.ANY) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.creatureName(ARG.any(arg), Control.Any);
        }
    },
    LandYouControlNamed("land you control named " + ARG.ANY) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.landName(ARG.any(arg), Control.You);
        }
    },
    PermanentCardGraveyard(ARG.WORDRUN + " permanent card from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Graveyard);
        }
    },
    CreatureCardGraveyard(ARG.WORDRUN + " creature card from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCreatureCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Graveyard);
        }
    },
    CardFromGraveyard(ARG.WORDRUN + " card from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Graveyard);
        }
    },
    PermanentCardOppGraveyard(ARG.WORDRUN + " permanent card from an opponent's graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.OpponentsGraveyard);
        }
    },
    CreatureCardOppGraveyard(ARG.WORDRUN + " creature card from an opponent's graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCreatureCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.OpponentsGraveyard);
        }
    },
    CardFromOppGraveyard(ARG.WORDRUN + " card from an opponent's graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.OpponentsGraveyard);
        }
    },
    PermanentCardHand(ARG.WORDRUN + " permanent card from your hand") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Hand);
        }
    },
    CreatureCardHand(ARG.WORDRUN + " creature card from your hand") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCreatureCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Hand);
        }
    },
    CardFromHand(ARG.WORDRUN + " card from your hand") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Hand);
        }
    },
    PermanentCardLibrary(ARG.WORDRUN + " permanent card from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Library);
        }
    },
    CreatureCardLibrary(ARG.WORDRUN + " creature card from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCreatureCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Library);
        }
    },
    CardLibrary(ARG.WORDRUN + " card from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Library);
        }
    },
    CardFromAGraveyard(ARG.WORDRUN + " card from a graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCardPrefix(arg.group(), ARG.wordrun(arg), MagicTargetType.Graveyard).from(MagicTargetType.OpponentsGraveyard);
        }
    },
    AttackingOrBlocking("attacking or blocking " + ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.permanentOr(MagicPermanentState.Attacking, MagicPermanentState.Blocking, MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)));
        }
    },
    Attacking("attacking " + ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.permanent(MagicPermanentState.Attacking, MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)));
        }
    },
    Untapped("untapped " + ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.untapped(MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)));
        }
    },
    Tapped("tapped " + ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.permanent(MagicPermanentState.Tapped, MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)));
        }
    },
    CreatureYouControlWith("creature you control with " + ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), "creature with " + ARG.wordrun(arg), Control.You);
        }
    },
    CreatureOppControlWith("creature an opponent controls with " + ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), "creature with " + ARG.wordrun(arg), Control.Opp);
        }
    },
    CreatureDefControlWith("creature defending player controls with " + ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), "creature with " + ARG.wordrun(arg), Control.Def);
        }
    },
    CreatureYouControl(ARG.WORDRUN + " creature you control") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCreaturePrefix(arg.group(), ARG.wordrun(arg), Control.You);
        }
    },
    CreatureOppControl(ARG.WORDRUN + " creature (an opponent controls|you don't control)") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCreaturePrefix(arg.group(), ARG.wordrun(arg), Control.Opp);
        }
    },
    PermanentYouControl(ARG.WORDRUN + "( permanent)? you control") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), ARG.wordrun(arg), Control.You);
        }
    },
    PermanentOppControl(ARG.WORDRUN + "( permanent)? (an opponent controls|you don't control)") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), ARG.wordrun(arg), Control.Opp);
        }
    },
    PermanentDefControl(ARG.WORDRUN + "( permanent)? defending player controls") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), ARG.wordrun(arg), Control.Def);
        }
    },
    Permanent(ARG.WORDRUN + " permanent") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), ARG.wordrun(arg), Control.Any);
        }
    },
    Creature(ARG.WORDRUN + " creature") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchCreaturePrefix(arg.group(), ARG.wordrun(arg), Control.Any);
        }
    },
    Planeswalker(ARG.WORDRUN + " planeswalker") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return  MagicTargetFilterFactory.matchPlaneswalkerPrefix(arg.group(), ARG.wordrun(arg), Control.Any);
        }
    },
    Spell(ARG.WORDRUN + " spell") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchSpellPrefix(arg.group(), ARG.wordrun(arg));
        }
    },
    PermanentAlt(ARG.WORDRUN) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicTargetFilterFactory.matchPermanentPrefix(arg.group(), ARG.wordrun(arg), Control.Any);
        }
    },
    ;

    private final Pattern pattern;

    private MagicTargetFilterParser(final String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    public Matcher matcher(final String rule) {
        return pattern.matcher(rule);
    }

    public abstract MagicTargetFilter<?> toTargetFilter(final Matcher arg);

    public static final MagicTargetFilter<?> build(final String text) {
        for (final MagicTargetFilterParser rule : values()) {
            final Matcher matcher = rule.matcher(text);
            if (matcher.matches()) {
                return rule.toTargetFilter(matcher);
            }
        }
        throw new RuntimeException("unknown target filter \"" + text + "\"");
    }
}
