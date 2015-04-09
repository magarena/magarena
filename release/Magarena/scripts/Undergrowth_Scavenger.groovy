[
    new MagicWhenComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = game.filterCards(permanent.getController(), CREATURE_CARD_FROM_ALL_GRAVEYARDS).size();
            game.doAction(MagicChangeCountersAction.Enters(permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    }
]
