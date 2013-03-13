[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            final int amount = player.getOpponent().getNrOfPermanentsWithType(MagicType.Creature);
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.MinusOne,
                amount,
                true
            ));
            return MagicEvent.NONE;
        }
    }
]
