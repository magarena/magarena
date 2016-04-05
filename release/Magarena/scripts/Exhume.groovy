def EVENT_ACTION = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(new ReanimateAction(
            it,
            event.getPlayer()
        ));
    });
}

def choice = new MagicTargetChoice("a creature card from your graveyard");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player puts a creature card from " +
                "his or her graveyard onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    player,
                    choice,
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    EVENT_ACTION,
                    "PN puts a creature card from his or her graveyard onto the battlefield."
                ));
            }
        }
    }
]
