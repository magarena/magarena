def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        for (final MagicCard card : new MagicCardList(event.getPlayer().getGraveyard())) {
            game.doAction(new RemoveCardAction(card, MagicLocationType.Graveyard));
            game.doAction(new MoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
        }
    } else {
        game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller exiles all cards from his or her graveyard. PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice("Exile your graveyard?"),
                    it,
                    action,
                    "PN may\$ exile his or her graveyard. If PN doesn't, RN is countered."
                ));
            });
            game.doAction(new DrawAction(event.getPlayer(),1));
        }
    }
]
