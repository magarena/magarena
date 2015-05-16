[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.Scream,payedCost.getX()));
            return MagicEvent.NONE;
        }
    }
]
