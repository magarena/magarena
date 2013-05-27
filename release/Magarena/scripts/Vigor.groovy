[
    new MagicIfDamageWouldBeDealtTrigger(4) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target=damage.getTarget();
            final int amount=damage.getAmount();
            return 
                !damage.isUnpreventable() &&
                amount > 0 && 
                target != permanent && 
                target.isCreature() && 
                permanent.isFriend(target);
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount=damage.getAmount();
        
            // Prevention effect.
            damage.setAmount(0);
            
            return new MagicEvent(
                permanent,
                damage.getTarget(),
                {
                    MagicGame G, MagicEvent E -> 
                    G.doAction(new MagicChangeCountersAction(
                        E.getRefPermanent(),
                        MagicCounterType.PlusOne,
                        amount,
                        true
                    ));
                } as MagicEventAction ,
                "Put "+amount+" +1/+1 counters on RN."
            );
        }
    }
]
