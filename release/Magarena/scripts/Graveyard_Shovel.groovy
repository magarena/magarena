def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game,new MagicCardAction() {
        public void doAction(final MagicCard card) {
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
            if (card.getCardDefinition().isCreature()) {
                game.doAction(new MagicChangeLifeAction(event.getRefPlayer(),2));
            }
        }
    });
} as MagicEventAction

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Exile"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source,"{2}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$ exiles a card from his or her graveyard. " +
                "If it's a creature card, PN gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer targetPlayer ->
                if (targetPlayer.getGraveyard().size() > 0) {
                    final MagicPlayer player = event.getPlayer();
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        targetPlayer,
                        MagicTargetChoice.TARGET_CARD_FROM_GRAVEYARD,
                        MagicGraveyardTargetPicker.ExileOwn,
                        player,
                        action,
                        "PN exiles a card\$ from his or her graveyard."
                    ));
                }
            } as MagicPlayerAction);
        }
    }
]
