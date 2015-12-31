[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            if (permanent.hasState(MagicPermanentState.CastFromHand)) {
                game.doAction(new ChangeCountersAction(permanent, MagicCounterType.Divinity, 1));
            }
            return MagicEvent.NONE;
        }
    }
]
