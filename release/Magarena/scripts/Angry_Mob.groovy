[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (MagicCondition.YOUR_TURN_CONDITION.accept(source)) {
                final int amount = source.getOpponent().getNrOfPermanents(MagicSubType.Swamp) + 2;
                pt.set(amount,amount);
            } else {
                pt.set(2,2);
            }
        }
    }
]
