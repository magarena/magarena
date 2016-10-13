[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates two 1/1 white Human creature tokens. " +
                "If PN has 5 or less life, he or she creates five of those tokens instead."
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
