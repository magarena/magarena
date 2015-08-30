[
    new MagicWhenTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            if (otherPermanent == permanent) {
                game.doAction(new ChangeCountersAction(permanent, MagicCounterType.PlusOne, 5));
            }
            return MagicEvent.NONE;
        }
    }
]
