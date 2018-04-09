[
    new IfCounterWouldChangeTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final ChangeCountersAction action) {
            return action.getObj().isCreaturePermanent() &&
                permanent.isFriend(action.getObj()) &&
                action.getCounterType() == MagicCounterType.PlusOne;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeCountersAction action) {
            action.setAmount(2 * action.getAmount());
            return MagicEvent.NONE;
        }
    }
]

