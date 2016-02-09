[
    new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.clear();
            flags.add(MagicSubType.Demon);
            flags.add(MagicSubType.Spirit);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
