[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            if (game.getCreatureDiedThisTurn()) {
                game.doAction(new ChangeCountersAction(permanent,MagicCounterType.PlusOne,4));
            }
            return MagicEvent.NONE;
        }
    }
]
