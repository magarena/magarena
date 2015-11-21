[
    new MagicStatic(MagicLayer.Type) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags & ~MagicType.Creature.getMask();
        }
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.remove(MagicSubType.God);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().getDevotion(MagicColor.Black) < 5;
        }
    },
    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            if (permanent.isOpponent(act.getPlayer()) && act.getLifeChange() > 0) {
                act.setLifeChange(0);
            }
            return MagicEvent.NONE;
        }
    }
]
