[
    new MagicStatic(MagicLayer.Type) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            if (permanent.hasSubType(MagicSubType.Vehicle)) {
                return MagicType.Creature.getMask()|flags;
            } else {
                return flags;
            }
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
