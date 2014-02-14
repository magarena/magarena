def SNOW_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isLand() && target.hasType(MagicType.Snow) && target.isController(player);
    }
};

[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = game.filterPermanents(player,SNOW_LAND_YOU_CONTROL).size();
            pt.set(size, size);
        }
    }
]
