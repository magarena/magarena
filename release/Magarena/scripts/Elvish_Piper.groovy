[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Put"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.CREATURE_CARD_FROM_HAND,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN puts a creature card\$ from his or her hand onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                    game.doAction(new MagicPlayCardAction(card,event.getPlayer()));
                }
            });
        }
    }
]
