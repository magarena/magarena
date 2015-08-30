[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(MagicConditionFactory.CounterAtLeast(MagicCounterType.Lore,1))],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Target creature PN controls\$ gets +1/+1 until end of turn for each lore counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPermanent().getCounters(MagicCounterType.Lore);
                game.logAppendValue(event.getPlayer(),amount);
                if (amount > 0) {
                    game.doAction(new ChangeTurnPTAction(it, amount, amount));
                }
            });
        }
    }
]
