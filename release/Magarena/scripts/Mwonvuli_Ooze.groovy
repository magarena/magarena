[
    new MagicCDA() {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPlayer player,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int amount = permanent.getCounters(MagicCounterType.Charge) * 2 + 1;
            pt.set(amount,amount);
        }
    }
]
