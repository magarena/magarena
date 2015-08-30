[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put X 1/1 white Bird Soldier creature tokens with flying on the battlefield, where X is the number of attacking creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = game.getTurnPlayer().getNrOfAttackers();
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 white Bird Soldier creature token with flying"),
                X
            ));
        }
    }
]
