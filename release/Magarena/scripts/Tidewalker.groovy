[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = ISLAND_YOU_CONTROL.filter(permanent).size();
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.Time,amount));
            return MagicEvent.NONE;
        }
    }
]
