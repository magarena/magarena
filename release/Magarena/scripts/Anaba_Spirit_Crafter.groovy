def MINOTAUR_CREATURE = new MagicTargetFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        return (target.isCreature() && target.hasSubType(MagicSubType.Minotaur));
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Permanent;
    }
};

[ 
	new MagicStatic(
		MagicLayer.ModPT,
        MINOTAUR_CREATURE
	) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,0); 
        }
    }
]