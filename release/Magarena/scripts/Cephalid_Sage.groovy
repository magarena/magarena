[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return (MagicCondition.THRESHOLD_CONDITION.accept(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws three cards, then discards two cards."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),3));
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),2));
        }
    }
]
