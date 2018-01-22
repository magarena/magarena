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
                "PN draws cards equal to the greatest power among creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {

            final int amount = event.getPlayer().getPermanents().max({ it.getPower() }).getPower();
            game.doAction(new DrawAction(event.getPlayer(), amount));

            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice(new MagicTargetChoice(
                    card().cmcLEQ(5).from(MagicTargetType.Hand),
                    "a card with converted mana cost 5 or less from your hand"
                )),
                action,
                "PN may\$ cast a card with converted mana cost 5 or less from PN's hand\$ without paying its mana cost."
            ));
        }
    }
]

