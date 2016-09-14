[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(MagicCondition.IS_BLOCKED_CONDITION)],
        new MagicActivationHints(MagicTiming.Pump),
        "Weaken"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPlayAbilityEvent(source),
                new MagicPayManaCostEvent(source, "{1}{W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each creature without flanking blocking SN gets -1/-1 until end of turn. "+
                "Activate this ability only once each turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent permanent : event.getPermanent().getBlockingCreatures()) {
                if (permanent.hasAbility(MagicAbility.Flanking) == false) {
                    game.doAction(new ChangeTurnPTAction(permanent, -1, -1));
                }
            }
        }
    }
]
