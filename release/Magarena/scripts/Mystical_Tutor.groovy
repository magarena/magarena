def INSTANT_OR_SORCERY_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Instant) || target.hasType(MagicType.Sorcery);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def AN_INSTANT_OR_SORCERY_CARD_FROM_LIBRARY = new MagicTargetChoice(
    INSTANT_OR_SORCERY_CARD_FROM_LIBRARY,
    "an instant or sorcery card"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for an instant or sorcery card, reveals it, shuffle his or her library, and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoLibraryEvent(
                event,
                AN_INSTANT_OR_SORCERY_CARD_FROM_LIBRARY
            ));
        }
    }
]
