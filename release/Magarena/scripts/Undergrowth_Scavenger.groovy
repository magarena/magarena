[
    new MagicWhenComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(game).size();
            game.doAction(ChangeCountersAction.Enters(permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    }
]
