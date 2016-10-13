[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN and each other player (${permanent.getController().getOpponent().getName()}) "+
                "creates a 3/3 black Angel creature token with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(
                event.getPermanent().getCard(),
                MagicLocationType.Graveyard,
                MagicLocationType.Exile
            ));
            game.doAction(new PlayTokenAction(
                event.getPlayer().getOpponent(),
                CardDefinitions.getToken("3/3 black Angel creature token with flying")
            ))
        }
    }
]
