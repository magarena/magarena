[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.Negative("target creature card from a graveyard"),
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Exile target creature card\$ from a graveyard. " +
                "Put a 2/2 black Zombie creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicPlayer player=event.getPlayer();
                game.doAction(new MagicRemoveCardAction(
                    it,
                    MagicLocationType.Graveyard
                ));
                game.doAction(new MagicMoveCardAction(
                    it,
                    MagicLocationType.Graveyard,
                    MagicLocationType.Exile
                ));
                game.doAction(new MagicPlayTokenAction(
                    player,
                    TokenCardDefinitions.get("2/2 black Zombie creature token")
                ));
            });
        }
    }
]
