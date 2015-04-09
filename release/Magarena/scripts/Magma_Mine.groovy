[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(
            MagicConditionFactory.CounterAtLeast(MagicCounterType.Pressure,1)
        )],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Pressure);
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                amount,
                this,
                "SN deals damage equal to the number of pressure counters on it to target creature or player\$. ("+amount+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.getRefInt();
                game.doAction(new MagicDealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
