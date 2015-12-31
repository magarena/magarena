[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getOpponent().controlsPermanent(UNTAPPED_LAND);
        }
    },

    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotBlock, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().controlsPermanent(UNTAPPED_LAND);
        }
    }

]
