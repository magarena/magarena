[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
		@Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
			MagicTargetChoice.POS_TARGET_CREATURE,
                this,
                "Target creature\$ gets +X/+X until end of turn, where X is the number of Elves on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
		event.processTargetPermanent(game, {
		final int amount =game.getNrOfPermanents(MagicSubType.Elf);
            game.doAction(new MagicChangeTurnPTAction(it,+amount,+amount));
	});
        }
    }
]
