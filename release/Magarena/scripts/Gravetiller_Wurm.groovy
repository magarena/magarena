[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            if (game.getCreatureDiedThisTurn()) {
                game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,4));
            }
            return MagicEvent.NONE;
        }
    }
]
