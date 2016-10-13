[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates two 2/2 green Bear creature tokens. " +
                "PN creates four 2/2 green Bear creature tokens if seven or more cards are in his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("2/2 green Bear creature token"),
                MagicCondition.THRESHOLD_CONDITION.accept(event.getSource()) ? 4 : 2
            ));
        }
    }
]
