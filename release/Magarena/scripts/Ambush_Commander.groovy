[
    new MagicStatic(
        MagicLayer.SetPT,
        FOREST_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(1,1);
        }
    },
    new MagicStatic(
        MagicLayer.Color,
        FOREST_YOU_CONTROL
    ) {
        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return flags|MagicColor.Green.getMask();
        }
    },
    new MagicStatic(
        MagicLayer.Type,
        FOREST_YOU_CONTROL
    ) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Elf);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    }
]
