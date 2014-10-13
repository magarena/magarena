def BLACK_PERMANENT=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.Black) && 
               target.isPermanent();
    }
};

def BLUE_PERMANENT=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.Blue) && 
               target.isPermanent();
    }
};

def GREEN_PERMANENT=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.Green) && 
               target.isPermanent();
    }
};

def RED_PERMANENT=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.Red) && 
               target.isPermanent();
    }
};

def WHITE_PERMANENT=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.White) && 
               target.isPermanent();
    }
};

[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            if (source.getGame().getNrOfPermanents(BLACK_PERMANENT) >=
                source.getGame().getNrOfPermanents(BLUE_PERMANENT) +
                source.getGame().getNrOfPermanents(GREEN_PERMANENT) +
                source.getGame().getNrOfPermanents(RED_PERMANENT) +
                source.getGame().getNrOfPermanents(WHITE_PERMANENT)) {
            pt.add(-2,-2);
            }
        }
    }
]
