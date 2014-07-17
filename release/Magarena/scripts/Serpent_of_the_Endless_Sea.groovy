[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = game.filterPermanents(player,MagicTargetFilterFactory.FOREST_YOU_CONTROL).size();
            pt.set(size, size);
        }
    },
    
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return !source.getController().getOpponent().controlsPermanent(MagicSubType.Island);
        }
    }
]
