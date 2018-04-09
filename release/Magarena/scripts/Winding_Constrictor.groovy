[
    new IfCounterWouldChangeTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final ChangeCountersAction action) {
            final MagicObject obj = action.getObj();
            return action.getAmount() > 0 &&
                (obj.isPlayer() || obj.hasType(MagicType.Creature) || obj.hasType(MagicType.Artifact)) &&
                permanent.isFriend(obj);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeCountersAction action) {
            action.setAmount(action.getAmount() + 1);
            return MagicEvent.NONE;
        }
    }
]

