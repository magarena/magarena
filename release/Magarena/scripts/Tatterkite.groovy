[
    new IfCounterWouldChangeTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final ChangeCountersAction action) {
            return action.getAmount() > 0 && action.getObj() == permanent;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeCountersAction action) {
            action.setAmount(0);
            return MagicEvent.NONE;
        }
    }
]

