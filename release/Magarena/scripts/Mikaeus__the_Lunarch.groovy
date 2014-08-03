[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Remove"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,1)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a +1/+1 counter on each other creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(creature.getController(),MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                if (target != creature) {
                    game.doAction(new MagicChangeCountersAction(target,MagicCounterType.PlusOne,1));
                }
            }
        }
    }
]
