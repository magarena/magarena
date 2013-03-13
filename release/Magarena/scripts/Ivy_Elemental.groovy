[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.PlusOne,
                permanent.getKicker(),
                true
            ));
            return MagicEvent.NONE;
        }
    }
]
