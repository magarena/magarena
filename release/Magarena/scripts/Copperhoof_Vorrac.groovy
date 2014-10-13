def UNTAPPED_PERMANENT=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isPermanent() && 
               target.isUntapped();
    }
};
[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = permanent.getOpponent().getNrOfPermanents(UNTAPPED_PERMANENT);
            pt.add(amount,amount);
        }
    }
]
