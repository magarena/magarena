[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{R}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(source.getCounters(MagicCounterType.PlusOne)),
                this,
                "SN deals damage to target creature\$ equals to the number of +1/+1 counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPermanent().getCounters(MagicCounterType.PlusOne)
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
