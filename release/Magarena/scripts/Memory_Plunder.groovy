[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(
                    TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ cast target instant or sorcery card an opponent's graveyard\$ without paying its mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(CastCardAction.WithoutManaCost(
                        event.getPlayer(),
                        it,
                        MagicLocationType.Graveyard,
                        MagicLocationType.Graveyard
                    ));
                });
            }
        }
    }
]
