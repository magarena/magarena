[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return permanent.isAttacking() ?
                flags | MagicAbility.FirstStrike.getMask() :
                flags;
        }
    }
]
