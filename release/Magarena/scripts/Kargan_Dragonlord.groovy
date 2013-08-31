[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 8) {
                pt.set(8,8);
            } else if (charges >= 4) {
                pt.set(4,4);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 8) {
                flags.add(MagicAbility.Trample);
            }
            if (charges >= 4) {
                flags.add(MagicAbility.Flying);
            }
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ChargeCountersAtLeast(8),
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "+1/+0"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +1/+0 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),1,0));
        }
    }
]
