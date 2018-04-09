[
    new IfCounterWouldChangeTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final ChangeCountersAction action) {
            return action.getAmount() > 0 &&
                action.getCounterType() == MagicCounterType.MinusOne &&
                action.getObj().isCreaturePermanent() &&
                permanent.isFriend(action.getObj());
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeCountersAction action) {
            action.setAmount(action.getAmount() - 1);
            return MagicEvent.NONE;
        }
    }
]

