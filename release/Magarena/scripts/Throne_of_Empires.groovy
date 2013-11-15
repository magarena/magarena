[
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source, "{1}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getController(),
                this,
                "PN Put a 1/1 white Soldier creature token onto the battlefield." + 
				"Put 5 of those tokens onto the battlefield instead if you control " + 
				"artifacts named Crown of Empires and Scepter of Empires."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			final MagicTargetFilter<MagicPermanent> crown = new MagicTargetFilter.NameTargetFilter("Crown of Empires");
			final MagicTargetFilter<MagicPermanent> scepter = new MagicTargetFilter.NameTargetFilter("Scepter of Empires");
			final MagicSource source = event.getSource();
			final MagicPlayer player = source.getController();
			final int amount = (player.controlsPermanent(crown) && player.controlsPermanent(scepter))? 5 : 1;
            game.doAction(new MagicPlayTokensAction(event.getPlayer(), TokenCardDefinitions.get("1/1 white Soldier creature token"),amount));
			
        }
    }
]