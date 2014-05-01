[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack spell) {
            return new MagicEvent(
                spell,
                new MagicMayChoice(
                    MagicTargetChoice.TARGET_CREATURE_CARD_POWER_2_OR_LESS_FROM_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ return target creature card\$ with power 2 or less from " +
                "his or her graveyard to the battlefield."
            );

            return new MagicEvent(
                spell,
                new MagicMayChoice(
                    MagicTargetChoice.TARGET_CREATURE_CARD_POWER_2_OR_LESS_FROM_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ return target creature card\$ with power 2 or less from " +
                "his or her graveyard to the battlefield."
            );            
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    final MagicCard card ->
                    game.doAction(new MagicReanimateAction(
                        card,
                        event.getPlayer()
                    ));
                });
            }
        }
    }
]
