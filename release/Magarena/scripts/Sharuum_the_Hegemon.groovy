[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.TARGET_ARTIFACT_CARD_FROM_GRAVEYARD,
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ return target artifact card\$ from your graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicReanimateAction(
                            card,
                            event.getPlayer()
                        ));
                    }
                });
            }
        }
    }
]
