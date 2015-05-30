[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = INSTANT_OR_SORCERY_CARD_FROM_ALL_GRAVEYARDS.filter(permanent.getController()).size();
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    }
]
