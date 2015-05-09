[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            CREATURE_YOU_CONTROL.except(permanent).filter(player) each {
                amount += it.getConvertedCost();
            }
            pt.set(amount, amount);
        }
    }
]
