[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getX(),
                this,
                "PN creates RN RN/RN green Ooze creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getRefInt();
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken(x, x, "green Ooze creature token"),
                x
            ));
        }
    }
]
