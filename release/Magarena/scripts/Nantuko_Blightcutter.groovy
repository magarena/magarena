[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            if (MagicCondition.THRESHOLD_CONDITION.accept(permanent)) {
				final int amt = permanent.getController().getOpponent().getNrOfPermanents(MagicTargetFilterFactory.BLACK_PERMANENT);
                pt.add(amt,amt);
            }
        }
    }
]