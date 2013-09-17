[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "+1/+1"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicExileCardPayManaCostEvent(
                    source, 
                    MagicTargetChoice.A_PAYABLE_CREATURE_CARD_FROM_YOUR_GRAVEYARD
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                payedCost.getTarget(),
                this,
                "Put a number of +1/+1 counters equal to that RN's power on target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    final int amt = event.getRefCard().genPowerToughness().getPositivePower();
                    if (amt > 0) {
                        game.doAction(new MagicChangeCountersAction(
                            permanent,
                            MagicCounterType.PlusOne,
                            amt,
                            true
                        ));
                    }
                }
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                    source
                ),
                MagicTargetHint.None,
                "a creature other than " + source + " to sacrifice"
            );
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    targetChoice
                ),
                new MagicRegenerationConditionsEvent(source,this)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Regenerate SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRegenerateAction(event.getPermanent()));
        }
    }
]
