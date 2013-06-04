[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_ZOMBIE_YOU_CONTROL,permanent);
            final int amount = game.filterPermanents(player,targetFilter).size() +
                               game.filterCards(player,MagicTargetFilter.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD).size();
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.PlusOne,
                amount,
                true
            ));
            return MagicEvent.NONE;
        }
    },
    new MagicIfDamageWouldBeDealtTrigger(4) {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            if (!damage.isUnpreventable() && 
                damage.getAmount() > 0 && 
                damage.getTarget() == permanent) {
                
                // Prevention effect.
                damage.setAmount(0);

                return new MagicEvent(
                    permanent,
                    this,
                    "Remove a +1/+1 counter from SN."
                );
            }
            return MagicEvent.NONE;
        }    
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                -1,
                true
            ));
        }
    }
]
