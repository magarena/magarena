[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Infect, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getOpponent().getPoison() > 0;
        }
    }
]
