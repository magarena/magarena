[
    new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack spell) {
            return new MagicEvent(
                spell,
                new MagicMayChoice(
                    TARGET_CREATURE_CARD_FROM_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ return target creature card\$ from " +
                "his or her graveyard to the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new MagicReanimateAction(
                        it,
                        event.getPlayer()
                    ));
                });
            }
        }
    }
]
