[
    new MagicIfDamageWouldBeDealtTrigger(3) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            damage.setAmount(damage.getAmount() * 2);
            return MagicEvent.NONE;
        }
    }
]
