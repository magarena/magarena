def EVENT_ACTION = new MagicEventAction() {
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetCard(game, {
            final MagicCard card ->
            game.doAction(new MagicReanimateAction(
                card,
                event.getPlayer()
            ));
        });
    }
};

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
            for (final MagicPlayer player : game.getPlayersAPNAP()) {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    EVENT_ACTION,
                    "PN puts a creature card from his or her graveyard onto the battlefield."
                ));
            }
        }
    }
]
