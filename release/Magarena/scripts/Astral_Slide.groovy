[
    new MagicWhenOtherCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return new MagicEvent(
                permanent,
		new MagicMayChoice(
                MagicTargetChoice.TARGET_CREATURE
		),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile target creature\$. If you do, return that card to the " +
                "battlefield under its owner's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
	    if (event.isYes()) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicExileUntilEndOfTurnAction(it));
                });
	    }
        }
    }
]
