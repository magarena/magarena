[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            if (damage.getTarget() == permanent) {
                game.doAction(new MillLibraryAction(damage.getSource().getController(),amount));
            }
            return MagicEvent.NONE;
        }
    }
]
