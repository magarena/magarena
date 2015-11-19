def DEMON_CONDITION = MagicConditionFactory.YouControl(MagicTargetFilterFactory.permanent(MagicSubType.Demon, Control.You));
[
    new MagicPermanentActivation(
        [DEMON_CONDITION, new MagicArtificialCondition(MagicConditionFactory.CounterAtLeast(MagicCounterType.Devotion,1))],
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Devotion);
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(amount,amount),
                amount,
                this,
                "Target creature\$ gets -X/-X until end of turn, where X is the number of devotion counters "+
                "on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPermanent().getCounters(MagicCounterType.Devotion);
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new ChangeTurnPTAction(it, -amount, -amount));
            });
        }
    }
]
