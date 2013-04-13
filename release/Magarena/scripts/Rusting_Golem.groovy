[
    new MagicCDA() {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPlayer player,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int amount = permanent.getCounters(MagicCounterType.Charge);
            pt.set(amount,amount);
        }
    }
]
