[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Return"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    MagicTargetChoice.SACRIFICE_CREATURE
                ),
                new MagicBouncePermanentEvent(
                    source,
                    source
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card\$ from your graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicReanimateAction(
                        targetCard,
                        event.getPlayer()
                    ));
                }
            });
        }
    }
]
