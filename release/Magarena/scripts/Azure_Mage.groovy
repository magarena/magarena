[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{3}{U}")],
        new MagicActivationHints(MagicTiming.Draw,true),
        "Draw"
        ) {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{3}{U}"))];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    }
]
