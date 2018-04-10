[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = game.getSpellsCast();
            game.doAction(new ChangeCountersAction(permanent.getController(),permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    }
]
