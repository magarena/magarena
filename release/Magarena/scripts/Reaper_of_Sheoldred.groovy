[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent) {
                game.doAction(new MagicChangePoisonAction(damage.getSource().getController(),1));
            }
            return MagicEvent.NONE;
        }
    }
]
