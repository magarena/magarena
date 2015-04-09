[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Put"
    ) {
        @Override
        public Iterable getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                A_CREATURE_CARD_FROM_HAND,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may put a creature card\$ from PN's hand onto the battlefield. "+
                "That creature gains haste. Sacrifice the creature "+
                "at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                    game.doAction(new MagicPlayCardAction(
                        card,
                        event.getPlayer(),
                        [MagicPlayMod.HASTE, MagicPlayMod.SACRIFICE_AT_END_OF_TURN]
                    ));
                }
            })
        }
    }
]
