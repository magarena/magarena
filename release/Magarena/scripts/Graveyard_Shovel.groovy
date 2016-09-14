def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.Exile));
        if (it.hasType(MagicType.Creature)) {
            game.doAction(new ChangeLifeAction(event.getRefPlayer(),2));
        }
    });
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Exile"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Target player\$ exiles a card from his or her graveyard. " +
                "If it's a creature card, PN gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                if (it.getGraveyard().size() > 0) {
                    final MagicPlayer player = event.getPlayer();
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        it,
                        TARGET_CARD_FROM_GRAVEYARD,
                        MagicGraveyardTargetPicker.ExileOwn,
                        player,
                        action,
                        "PN exiles a card\$ from his or her graveyard."
                    ));
                }
            });
        }
    }
]
