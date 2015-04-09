
def LymphPrevent = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        if (damage.getTarget() == permanent) {
            // Prevention effect.
            damage.prevent(1);
        }
        return MagicEvent.NONE;
    }
};

[    
    new MagicStatic(
        MagicLayer.Ability,
        SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(LymphPrevent);
        }
    }
]
