[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int X = cardOnStack.getController().getNrOfPermanents(MagicType.Artifact);
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(-X, -X),
                this,
                "Target creature\$ gets -1/-1 until end of turn for each artifact you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = event.getPlayer().getNrOfPermanents(MagicType.Artifact);
                game.doAction(new MagicChangeTurnPTAction(it, -X, -X));
            });
        }
    }
]
