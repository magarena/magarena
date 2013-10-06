[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source, "{2}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.Negative("target creature card from a graveyard"),
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Exile target creature card\$ from a graveyard. " +
                "Put a 2/2 black Zombie creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    final MagicPlayer player=event.getPlayer();
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.Graveyard
                    ));
                    game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.Exile
                    ));
                    game.doAction(new MagicPlayTokenAction(
                        player,
                        TokenCardDefinitions.get("Zombie")
                    ));
                }
            });
        }
    }
]
