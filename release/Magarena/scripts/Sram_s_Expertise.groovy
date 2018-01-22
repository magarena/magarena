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
                "PN creates three 1/1 colorless Servo artifact creature tokens."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {

            3.times {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 colorless Servo artifact creature token")
                ));
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


