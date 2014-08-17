[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                damage.getTarget().isPlayer() &&
                damage.isCombat()) ?
            for(int i=1;i<=2;i++){
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                        MagicGraveyardTargetPicker.ExileOpp,
                        this,
                        "exile target card from a graveyard"
                    )
                )
            }:
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent card ->
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
            });
        }
    }
]
