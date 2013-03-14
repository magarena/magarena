[
    new MagicIfDamageWouldBeDealtTrigger(5) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            final int amount=damage.getAmount();
            if (!damage.isUnpreventable()&&amount>0&&damage.getSource().getController()!=player&&damage.getTarget()==player) {
                // Prevention effect.
                damage.setAmount(amount-1);
            }            
            return MagicEvent.NONE;
        }
    }
]
