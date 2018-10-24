def action = {
    final MagicGame game, final MagicEvent event ->
        if (event.isYes()) {
        for (final MagicPlayer player : game.getAPNAP()) {
        final int x = event.getCardOnStack().getX();
            game.doAction(new PlayTokensAction(player, CardDefinitions.getToken("1/1 red Elemental creature token with haste"), x));
        }
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates " + x + " 1/1 red Elemental creature tokens with haste."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getCardOnStack().getX();
               game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("1/1 red Elemental creature token with haste"), x));
               game.addEvent(new MagicEvent(event.getSource(), event.cardOnStack.getOpponent(), new MagicMayChoice("Create tokens?"), action,
                "PN may\$ creates " + x + " 1/1 red Elemental creature tokens with haste. " +
                "If PN does, " + event.getPlayer() + " creates " + x + " 1/1 red Elemental creature tokens with haste."
            ));
        }
    }
]
