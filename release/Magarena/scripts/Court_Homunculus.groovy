[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            // incorrect implementation, should be ANOTHER artifact
            if (source.getController().controlsPermanent(MagicType.Artifact)) {
                pt.add(1,1);
            }
        }
    }
]
