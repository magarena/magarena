[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = (int)permanent.getController().getGraveyard().count({ it.hasAbility(MagicAbility.Cycling) });
            pt.add(amount, 0);
        }
    }
]

