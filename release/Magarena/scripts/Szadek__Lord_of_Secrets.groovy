[
    new MagicIfDamageWouldBeDealtTrigger(6) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getAmount();
            if (amount>0 && 
                damage.isCombat() && 
                permanent==damage.getSource() && 
                damage.getTarget().isPlayer()) {
                // Replacement effect.
                damage.setAmount(0);
                game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount,true));
                game.doAction(new MagicMillLibraryAction(damage.getTargetPlayer(),amount));
            }            
            return MagicEvent.NONE;
        }
    }
]
