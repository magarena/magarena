def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicLocationType toLocation = event.isYes() ? MagicLocationType.TopOfOwnersLibrary : MagicLocationType.OwnersHand;
    game.doAction(new RemoveFromPlayAction(event.getRefPermanent(), toLocation));
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONLAND_PERMANENT,
                this,
                "Ascend. Return target nonland permanent\$ to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(MagicRuleEventAction.create("ascend").getEvent(event));
            event.processTargetPermanent(game, {
                if (event.getPlayer().hasState(MagicPlayerState.CitysBlessing)) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        new MagicMayChoice("Put that permanent on top of its owner's library instead?"),
                        it,
                        action,
                        "If PN has the city's blessing, " +
                        "PN may\$ put that permanent on top of its owner's library instead."
                    ));
                } else {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        it,
                        action,
                        ""
                    ));
                }
            }
        }
    }
]


