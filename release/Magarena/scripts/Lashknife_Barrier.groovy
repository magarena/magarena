[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget().isFriend(permanent) && damage.getTarget().isCreaturePermanent()) {
                damage.setAmount(damage.getAmount()-1);
            }
            return MagicEvent.NONE;
        }
    }
]
