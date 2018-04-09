[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "+1/+1"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicExileCardPayManaCostEvent(
                    source,
                    A_PAYABLE_CREATURE_CARD_FROM_YOUR_GRAVEYARD
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                payedCost.getTarget(),
                this,
                "Put a number of +1/+1 counters equal to that RN's power on target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amt = event.getRefCard().genPowerToughness().getPositivePower();
                game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.PlusOne,amt));
            });
        }
    }
]
