[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            damage.setAmount((int)Math.floor(damage.getAmount()/2));
            return MagicEvent.NONE;
        }
    }
]
