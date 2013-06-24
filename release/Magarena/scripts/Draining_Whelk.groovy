[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_SPELL,
                this,
                "Counter target spell\$. Put X +1/+1 counters on SN, " +
                "where X is that spell's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack card) {
                    game.doAction(new MagicCounterItemOnStackAction(card));
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        card.getConvertedCost(),
                        true
                    ));
                }
            });
        }
    }
]
