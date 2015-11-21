[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().isFriend(permanent) && 
                damage.getSource() != permanent && 
                damage.getSource().hasColor(MagicColor.Red)
            ) {
                damage.setAmount(damage.getAmount()+1);
            }
            return MagicEvent.NONE;
        }
    }
]
