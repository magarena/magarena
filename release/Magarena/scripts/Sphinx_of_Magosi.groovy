[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{2}{U}")],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{2}{U}"))];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Draw a card, then put a +1/+1 counter on SN."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1,true));
        }
    }
]
