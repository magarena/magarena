[
    new MagicCDA() {
        @Override
        public void getSubTypeFlags(final MagicGame game, final MagicPlayer player, final Set<MagicSubType> flags) {
            flags.addAll(MagicSubType.ALL_CREATURES);
        }
    }
]
