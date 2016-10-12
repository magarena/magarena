[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN creates a 1/1 white Soldier creature token." +
                "Put 5 of those tokens onto the battlefield instead if PN controls " +
                "artifacts named Crown of Empires and Scepter of Empires."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> crown = new MagicNameTargetFilter("Crown of Empires");
            final MagicTargetFilter<MagicPermanent> scepter = new MagicNameTargetFilter("Scepter of Empires");
            final MagicSource source = event.getSource();
            final MagicPlayer player = source.getController();
            final int amount = (player.controlsPermanent(crown) && player.controlsPermanent(scepter))? 5 : 1;
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("1/1 white Soldier creature token"),amount));

        }
    }
]
