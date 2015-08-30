[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = permanent.getController().getOpponent().getPoison();
            pt.add(amount,amount);
        }
    }
]
