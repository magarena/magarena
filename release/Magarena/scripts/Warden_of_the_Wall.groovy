[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(2,3);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.NOT_YOUR_TURN_CONDITION.accept(source);
        }
    },
    new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Gargoyle);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags |
                   MagicType.Artifact.getMask() |
                   MagicType.Creature.getMask();
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.NOT_YOUR_TURN_CONDITION.accept(source);
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Flying, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.NOT_YOUR_TURN_CONDITION.accept(source);
        }
    }

]
