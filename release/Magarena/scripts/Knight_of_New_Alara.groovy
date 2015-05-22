[
    new MagicStatic(MagicLayer.ModPT, CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            for (final MagicColor color : MagicColor.values()) {
                if (permanent.hasColor(color) && permanent != source) {
                    amount += 1;
                }
            }
            if (amount >= 2) {
                pt.add(amount,amount);
            }
        }
    }
]
