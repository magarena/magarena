[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = permanent.getCounters(MagicCounterType.Fade);
            pt.set(amount,amount);
        }
    }
]
