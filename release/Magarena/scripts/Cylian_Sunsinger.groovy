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
                "SN and each other creature with the same name as it get +3/+3 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> targetFilter =
                new MagicNameTargetFilter(
                    CREATURE,
                    event.getPermanent().getName()
                );
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(), targetFilter);
            for (final MagicPermanent permanent : targets) {
                game.doAction(new ChangeTurnPTAction(permanent,3,3));
            }
        }
    }
]
