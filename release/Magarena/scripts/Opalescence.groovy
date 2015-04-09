[
    new MagicStatic(MagicLayer.SetPT, NON_AURA_ENCHANTMENT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int cmc = permanent.getConvertedCost();
            pt.set(cmc,cmc);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicStatic(MagicLayer.Type, NON_AURA_ENCHANTMENT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent, final int flags) {
            return flags | MagicType.Creature.getMask();
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    }
]
