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
            CREATURE_YOU_CONTROL
            .except(event.getPermanent())
            .filter(event.getPlayer()) each {
                game.doAction(new ChangeCountersAction(it,MagicCounterType.PlusOne,1));
            }
        }
    }
]
