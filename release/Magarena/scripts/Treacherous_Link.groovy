[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent.getEnchantedPermanent()) {
                damage.setTarget(permanent.getEnchantedPermanent().getController());
                damage.setAmount(damage.replace());
            }
            return MagicEvent.NONE;
        }
    }
]
