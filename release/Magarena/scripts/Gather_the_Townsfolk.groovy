[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts two 1/1 white Human creature tokens onto the battlefield. " +
                "If PN has 5 or less life, he or she puts five of those tokens onto the battlefield instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int amount = MagicCondition.FATEFUL_HOUR.accept(event.getSource()) ? 5 : 2;
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 white Human creature token"),
                amount
            ));
        }
    }
]
