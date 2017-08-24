[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals 1 damage to each creature with flying PN's opponents control. Tap those creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(),it,1));
                game.doAction(new TapAction(it));
            }
        }
    }
]
