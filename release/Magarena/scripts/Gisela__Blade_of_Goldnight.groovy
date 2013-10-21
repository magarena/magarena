[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getAmount();
            if (permanent.isEnemy(damage.getTarget())) {
                // Generates no event or action.
                damage.setAmount(amount * 2);
            }else{
                int half = (int) Math.ceil((double)amount / 2);
                // Prevention effect.
                damage.prevent(half);
            }
            return MagicEvent.NONE;
        }
    }
]
