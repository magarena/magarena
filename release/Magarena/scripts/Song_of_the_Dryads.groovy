[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
            permanent.addAbility(MagicTapManaActivation.Green);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.clear();
            flags.add(MagicSubType.Forest);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return MagicType.Land.getMask()
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    new MagicStatic(MagicLayer.Color) {
        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return 0;
        }
    }
]
