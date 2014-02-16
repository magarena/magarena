[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches PN's library for an equipment card, reveals it, puts it into PN's hand, and shuffles PN's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                MagicTargetChoice.EQUIPMENT_CARD_FROM_LIBRARY
            ));
        }
    }
]
