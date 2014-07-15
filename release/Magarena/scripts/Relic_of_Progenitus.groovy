def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
    });
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Exile one"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$ exiles a card from his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer targetPlayer ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    targetPlayer,
                    new MagicTargetChoice("a card from your graveyard"),
                    MagicGraveyardTargetPicker.ExileOwn,
                    action,
                    "PN exiles a card\$ from his or her graveyard."
                ));
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Exile all"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicExileEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile all cards from all graveyards. Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayersAPNAP()) {
                for (final MagicCard card : new MagicCardList(player.getGraveyard())) {
                    game.doAction(new MagicRemoveCardAction(card, MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
                }
            }
            game.doAction(new MagicDrawAction(event.getPlayer()));
        }
    }
]
