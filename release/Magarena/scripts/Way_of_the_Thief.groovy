[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (permanent.getController().controlsPermanent(MagicSubType.Gate)) {
                permanent.addAbility(MagicAbility.Unblockable, flags);
            };
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
