[
    new IfCounterWouldChangeTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final ChangeCountersAction action) {
            return action.getAmount() > 0 &&
                action.getCounterType() == MagicCounterType.PlusOne &&
                action.getObj().isCreaturePermanent() &&
                permanent.isFriend(action.getObj());
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeCountersAction action) {
            action.setAmount(2 * action.getAmount());
            return MagicEvent.NONE;
        }
    }
]

