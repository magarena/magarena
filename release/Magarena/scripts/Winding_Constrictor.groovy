[
    new IfCounterWouldChangeTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final ChangeCountersAction action) {
            final MagicObject obj = action.getObj();
            if (action.getAmount() > 0 && permanent.isFriend(obj)) {
                if (obj.isPermanent()) {
                    final MagicPermanent target = (MagicPermanent)obj;
                    return (target.hasType(MagicType.Creature) || target.hasType(MagicType.Artifact));
                } else if (obj.isPlayer()) {
                    return true;
                }
            }
            return false;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeCountersAction action) {
            action.setAmount(action.getAmount() + 1);
            return MagicEvent.NONE;
        }
    }
]

