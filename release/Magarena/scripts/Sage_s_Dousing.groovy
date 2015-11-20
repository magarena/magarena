[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {3}. " +
                "If PN controls a Wizard, he or she draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create("{3}")));
                final MagicPlayer you = event.getPlayer();
                if (you.controlsPermanent(MagicSubType.Wizard)) {
                    game.doAction(new DrawAction(you));
                }
            });
        }
    }
]
