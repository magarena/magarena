[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                    1,
                    MagicSimpleMayChoice.DEFAULT_YES
                ),
                this,
                "PN may\$ put a quest counter on SN."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.Charge,
                    1,
                    true
                ));
            }
        }        
    },
    new MagicPermanentActivation(
        [MagicConditionFactory.ChargeCountersAtLeast(3)],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,3),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws two cards"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),2));
        }
    }
]
