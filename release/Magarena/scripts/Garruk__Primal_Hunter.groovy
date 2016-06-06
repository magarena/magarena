[
    new MagicPlaneswalkerActivation(-3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Draw cards equal to the greatest power among creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int power = 0;
            CREATURE_YOU_CONTROL.filter(event) each {
                power = Math.max(power, it.getPower());
            }
            game.doAction(new DrawAction(
                event.getPlayer(),
                power
            ));
        }
    }
]
