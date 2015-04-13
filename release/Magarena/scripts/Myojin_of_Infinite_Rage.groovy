[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            if (permanent.hasState(MagicPermanentState.CastFromHand)) {
                game.doAction(new MagicChangeCountersAction(permanent, MagicCounterType.Divinity, 1));
            } 
            return MagicEvent.NONE;
        }
    }
]
