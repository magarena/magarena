[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
            permanent.addAbility(MagicAbility.Indestructible);
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
            flags.add(MagicSubType.Insect);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return MagicType.Creature.getMask()|MagicType.Artifact.getMask()
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.set(0,1);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
