[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = player.getLife();
			if (amount>=13){
				pt.set(0,0);
			}else{
				pt.set(13-amount,13-amount);
			}            
        }
    }
]