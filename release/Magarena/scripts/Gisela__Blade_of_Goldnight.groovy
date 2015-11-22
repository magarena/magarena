[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isEnemy(damage.getTarget())) {
                final int amount = damage.getAmount();
                damage.setAmount(amount * 2);
            }
            return MagicEvent.NONE;
        }
    },
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isFriend(damage.getTarget())) {
                final int amount = damage.getAmount();
                damage.prevent((amount + 1).intdiv(2));
            }
            return MagicEvent.NONE;
        }
    }
]
