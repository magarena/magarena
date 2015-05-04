[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return cardOnStack.getController().getNrOfPermanents(MagicType.Land) <= 4 ?
                new MagicEvent(
                    cardOnStack,
                    this,
                    "Search your library for a basic land card, put it onto the battlefield tapped, then shuffle your library."
                ):
                MagicEvent.NONE;
            }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(MagicRuleEventAction.create(
                "Search your library for a basic land card, put it onto the battlefield tapped, then shuffle your library.").getEvent(event.getSource())
            );
        }
    }
]
