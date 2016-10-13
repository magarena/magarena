[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{5}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN exiles target creature card from his or her graveyard\$, then creates a token "+
                "that's a copy of that card except it's 1/1, it's a Spirit in addition to its other types, and it has "+
                "flying. PN creates a black Zombie creature token with power equal to that card's "+
                "power and toughness equal to that card's toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard card ->
                game.doAction(new ExileLinkAction(event.getPermanent(), card, MagicLocationType.Graveyard));
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    MagicCardDefinition.token(
                        card,
                        {
                            it.setPowerToughness(1,1);
                            it.addSubType(MagicSubType.Spirit);
                            it.addAbility(MagicAbility.Flying);
                        }
                    )
                ));
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken(card.getPower(), card.getToughness(), "black Zombie creature token")
                ));
            });
        }
    }
]
