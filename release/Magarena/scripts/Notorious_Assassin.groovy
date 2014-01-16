[
	new MagicPermanentActivation(
		new MagicActivationHints(MagicTiming.Removal),
		"Destroy"
	) {

		@Override
		public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
			return [
				new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}{B}"), new MagicDiscardEvent(source, 1)
			];
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
			source,
			this,
			"Destroy target nonblack creature. It can't be regenerated."
			);
		}

		@Override
		public void executeEvent(final MagicGame game, final MagicEvent event) {			
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(creature));
            });
		}
	}
]

