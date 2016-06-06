[
    new MagicPlaneswalkerActivation(0) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 3 damage to target creature. That creature deals damage equal to its power to him."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,3));
                game.doAction(new DealDamageAction(it,event.getPermanent(),it.getPower()));
            });
        }
    }
]
