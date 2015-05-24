[   
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (damage.getTarget() == permanent && permanent.hasCounters(MagicCounterType.PlusOne)) {
                final int amount = permanent.getCounters(MagicCounterType.PlusOne);
                // Prevention effect.
                damage.prevent(amount);
            
                game.doAction(new ChangeCountersAction(permanent,MagicCounterType.PlusOne,-amount));
            }
            return MagicEvent.NONE;
        }
    }
]
//Rock Hydra's activated damage prevention should occur before this
