[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {1}. Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create("{1}")));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
