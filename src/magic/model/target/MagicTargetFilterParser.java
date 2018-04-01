package magic.model.target;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.model.ARG;
import magic.model.MagicPermanentState;
import magic.model.MagicType;
import magic.model.MagicSubType;
import magic.model.target.MagicTargetFilterFactory.Control;
import magic.exception.ScriptParseException;

import static magic.model.target.MagicTargetFilterFactory.*;

public enum MagicTargetFilterParser {
    CARD1("artifact card with converted mana cost " + ARG.NUMBER + " or less from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return card(MagicType.Artifact).cmcLEQ(ARG.number(arg)).from(MagicTargetType.Graveyard);
        }
    },
    CARD2("Rebel permanent card with converted mana cost " + ARG.NUMBER + " or less from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Graveyard, ARG.number(arg));
        }
    },
    CARD3("permanent card with converted mana cost " + ARG.NUMBER + " or less from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMaxCMC(MagicTargetType.Graveyard, ARG.number(arg));
        }
    },
    CARD4("creature card with converted mana cost " + ARG.NUMBER + " or less from your graveyard") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return card(MagicType.Creature).cmcLEQ(ARG.number(arg)).from(MagicTargetType.Graveyard);
        }
    },
    CARD5("instant card with converted mana cost " + ARG.NUMBER + " or less from your hand") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return card(MagicType.Instant).from(MagicTargetType.Hand).cmcLEQ(ARG.number(arg));
        }
    },
    CARD6("enchantment card with converted mana cost " + ARG.NUMBER + " or less from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMaxCMC(MagicType.Enchantment, MagicTargetType.Library, ARG.number(arg));
        }
    },
    CARD7("artifact card with converted mana cost " + ARG.NUMBER + " or less from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMaxCMC(MagicType.Artifact, MagicTargetType.Library, ARG.number(arg));
        }
    },
    CARD8("artifact card with converted mana cost " + ARG.NUMBER + " or greater from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMinCMC(MagicType.Artifact, MagicTargetType.Library, ARG.number(arg));
        }
    },
    CARD9("artifact card with converted mana cost " + ARG.NUMBER + " from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardEqualCMC(MagicType.Artifact, MagicTargetType.Library, ARG.number(arg));
        }
    },
    CARD10("Rebel permanent card with converted mana cost " + ARG.NUMBER + " or less from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, ARG.number(arg));
        }
    },
    CARD11("Mercenary permanent card with converted mana cost " + ARG.NUMBER + " or less from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, ARG.number(arg));
        }
    },
    CARD12("creature card with converted mana cost " + ARG.NUMBER + " or less from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMaxCMC(MagicType.Creature, MagicTargetType.Library, ARG.number(arg));
        }
    },
    CARD13("creature card with converted mana cost " + ARG.NUMBER + " or greater from your library") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanentCardMinCMC(MagicType.Creature, MagicTargetType.Library, ARG.number(arg));
        }
    },

    PERM2("non" + ARG.COLOR + " creature with power " + ARG.NUMBER + " or greater") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return new MagicPTTargetFilter(creatureNon(ARG.color(arg), Control.Any), Operator.GREATER_THAN_OR_EQUAL, ARG.number(arg));
        }
    },
    PERM1(ARG.COLOR + " creature with power " + ARG.NUMBER + " or greater") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return new MagicPTTargetFilter(creature(ARG.color(arg), Control.Any), Operator.GREATER_THAN_OR_EQUAL, ARG.number(arg));
        }
    },
    PERM3(ARG.NUMBER + "/" + ARG.NUMBER2 + " creature") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return new MagicPTTargetFilter(CREATURE, Operator.EQUAL, ARG.number(arg), Operator.EQUAL, ARG.number2(arg));
        }
    },
    PERM4("creature with converted mana cost " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return permanent(MagicType.Creature, Control.Any).cmcLEQ(ARG.number(arg));
        }
    },
    PERM5("creature with power " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return new MagicPTTargetFilter(CREATURE, Operator.LESS_THAN_OR_EQUAL, ARG.number(arg));
        }
    },
    PERM6("creature with power " + ARG.NUMBER + " or greater") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return new MagicPTTargetFilter(CREATURE, Operator.GREATER_THAN_OR_EQUAL, ARG.number(arg));
        }
    },
    PERM7("creature with toughness " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicPTTargetFilter.Toughness(CREATURE, Operator.LESS_THAN_OR_EQUAL, ARG.number(arg));
        }
    },
    PERM8("creature with toughness " + ARG.NUMBER + " or greater") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return MagicPTTargetFilter.Toughness(CREATURE, Operator.GREATER_THAN_OR_EQUAL, ARG.number(arg));
        }
    },
    PERM9("artifact or enchantment with converted mana cost " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return new MagicCMCPermanentFilter(ARTIFACT_OR_ENCHANTMENT, Operator.LESS_THAN_OR_EQUAL, ARG.number(arg));
        }
    },
    SPELL1("spell with converted mana cost " + ARG.NUMBER) {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return SPELL.cmcEQ(ARG.number(arg));
        }
    },
    SPELL2("spell with converted mana cost " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return SPELL.cmcLEQ(ARG.number(arg));
        }
    },
    SPELL3("spell with converted mana cost " + ARG.NUMBER + " or greater") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return SPELL.cmcGEQ(ARG.number(arg));
        }
    },
    SPELL4("instant spell you control with converted mana cost " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return spell(MagicType.Instant).youControl().cmcLEQ(ARG.number(arg));
        }
    },
    SPELL5("sorcery spell you control with converted mana cost " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return spell(MagicType.Sorcery).youControl().cmcLEQ(ARG.number(arg));
        }
    },
    SPELL6("creature spell with converted mana cost " + ARG.NUMBER + " or less") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return spell(MagicType.Creature).cmcLEQ(ARG.number(arg));
        }
    },
    SPELL7("creature spell with converted mana cost " + ARG.NUMBER + " or greater") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return spell(MagicType.Creature).cmcGEQ(ARG.number(arg));
        }
    },
    SPELL8("colorless spell with converted mana cost " + ARG.NUMBER + " or greater") {
        @Override
        public MagicTargetFilter<?> toTargetFilter(final Matcher arg) {
            return COLORLESS_SPELL.cmcGEQ(ARG.number(arg));
        }
    },
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
        throw new ScriptParseException("unknown target filter \"" + text + "\"");
    }
}
