[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{R}{G}{W}")];
        }

       @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN and all the creatures with the same name gets +2/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			final MagicTargetFilter<MagicPermanent> targetFilter =
				new MagicTargetFilter.NameTargetFilter(event.getPermanent().getName());
			final Collection<MagicPermanent> targets =
				game.filterPermanents(event.getPlayer(),targetFilter);
			for (final MagicPermanent permanent : targets) {
				if (permanent.isCreature()) {
					game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
				}
			}
        }
    }
]
