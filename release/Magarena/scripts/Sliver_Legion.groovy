[ 
    new MagicStatic(MagicLayer.ModPT, SLIVER) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount =
                source.getController().getNrOfPermanents(MagicSubType.Sliver) +
                source.getOpponent().getNrOfPermanents(MagicSubType.Sliver) -
                1;
            pt.add(amount,amount); 
        }
    }
]
