def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetCard(game, {
            game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), it, MagicLocationType.OwnersHand, MagicLocationType.Graveyard));
        });
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get -3/-3 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
            for (final MagicPermanent perm : player.getPermanents()) {
                if (perm.hasType(MagicType.Creature)) {
                    game.doAction(new ChangeTurnPTAction(perm, -3, -3));
                }
            }
            }

            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice(new MagicTargetChoice(
                    card().cmcLEQ(3).from(MagicTargetType.Hand),
                    "a card with converted mana cost 3 or less from your hand"
                )),
                action,
                "PN may\$ cast a card with converted mana cost 3 or less from PN's hand\$ without paying its mana cost."
            ));
        }
    }
]

