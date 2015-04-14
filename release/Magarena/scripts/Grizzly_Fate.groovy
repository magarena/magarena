[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put two 2/2 green Bear creature tokens onto the battlefield. " +
                "Put four 2/2 green Bear creature tokens onto the battlefield instead if seven or more cards are in your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("2/2 green Bear creature token"),
                MagicCondition.THRESHOLD_CONDITION.accept(event.getSource()) ? 4 : 2
            ));
        }
    }
]
