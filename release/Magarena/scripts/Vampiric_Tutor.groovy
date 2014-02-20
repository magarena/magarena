def CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return true;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def A_CARD_FROM_LIBRARY = new MagicTargetChoice(
    CARD_FROM_LIBRARY,
    "a card"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a card, shuffle his or her library, and put that card on top of it. PN loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
            game.addEvent(new MagicSearchOntoLibraryEvent(
                event,
                A_CARD_FROM_LIBRARY
            ));
        }
    }
]
