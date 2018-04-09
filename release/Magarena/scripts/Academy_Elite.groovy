[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = INSTANT_OR_SORCERY_CARD_FROM_ALL_GRAVEYARDS.filter(permanent.getController()).size();
            game.doAction(new ChangeCountersAction(permanent.getController(),permanent,MagicCounterType.PlusOne,amount));
            game.logAppendMessage(permanent.getController(), permanent.getName()+" enters the battlefield with "+amount+" +1/+1 counters on it.");
            return MagicEvent.NONE;
        }
    }
]
