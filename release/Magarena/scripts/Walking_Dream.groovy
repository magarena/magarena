[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (permanent.getOpponent().getNrOfPermanents(MagicType.Creature) >= 2) {
                permanent.addAbility(MagicAbility.DoesNotUntap, flags);
            }
        }
    }
]
