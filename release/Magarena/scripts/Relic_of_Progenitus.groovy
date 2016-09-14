def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.Exile));
    });
}

def choice = new MagicTargetChoice("a card from your graveyard");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Exile one"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Target player\$ exiles a card from his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    choice,
                    MagicGraveyardTargetPicker.ExileOwn,
                    action,
                    "PN exiles a card\$ from his or her graveyard."
                ));
            });
        }
    }
]
