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
                final int X = event.getPlayer().getOpponent().getNrOfAttackers() + event.getPlayer().getNrOfAttackers();
                game.doAction(new MagicPlayTokensAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 white Bird Soldier creature token with flying"),
                    X
                ));
        }
    }
]
