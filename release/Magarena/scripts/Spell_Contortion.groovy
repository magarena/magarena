[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                payedCost.getKicker(),
                this,
                "Counter target spell\$ unless its controller pays {2}. " +
                "PN draws a card for each time SN was kicked. (RN)"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create("{2}")));
                game.doAction(new DrawAction(event.getPlayer(),event.getRefInt()));
            });
        }
    }
]
