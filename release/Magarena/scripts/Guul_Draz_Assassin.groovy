[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 4) {
                pt.set(4,4);
            } else if (level >= 2) {
                pt.set(2,2);
            }
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.CounterAtLeast(MagicCounterType.Level,2),
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Level) >= 4 ? -4:-2;
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(-amount,-amount),
                amount,
                this,
                "Target creature\$ gets RN/RN until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,amount,amount));
            });
        }
    }
]
