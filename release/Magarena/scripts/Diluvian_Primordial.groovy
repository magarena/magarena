[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "For each opponent, PN may\$ cast up to one target instant or sorcery card from that player's graveyard\$ without paying its mana cost. "+
                "If a card cast this way would be put into a graveyard this turn, exile it instead."
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
                        MagicLocationType.Exile
                    ));
                });
            }
        }
    }
]
