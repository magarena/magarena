def ENCHANTMENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Enchantment);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def AN_ENCHANTMENT_CARD_FROM_LIBRARY = new MagicTargetChoice(
    ENCHANTMENT_CARD_FROM_LIBRARY,
    "an enchantment card"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may search his or her library for an enchantment card, reveal it, put it into his or her hand, and shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                AN_ENCHANTMENT_CARD_FROM_LIBRARY
            ));
        }
    }
]
