[
    new MagicSpellCardEvent() {
        @java.lang.Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {1} for each artifact PN controls."
            );
        }
        @java.lang.Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicType.Artifact);
                game.addEvent(new MagicCounterUnlessEvent(
                    event.getSource(),
                    it,
                    MagicManaCost.create(amount)
                ));
            });
        }
    }
]
