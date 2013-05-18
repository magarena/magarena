[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            if (game.getCreatureDiedThisTurn()) {
                game.doAction(new MagicChangeCountersAction(
                    permanent,
                    MagicCounterType.PlusOne,
                    2,
                    true
                ));
            }
            return MagicEvent.NONE;
        }
    }
]
