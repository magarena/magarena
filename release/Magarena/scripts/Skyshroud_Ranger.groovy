[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Land),
        "Land"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicMayChoice(
                    MagicTargetChoice.TARGET_LAND_CARD_FROM_HAND
                ),
                new MagicGraveyardTargetPicker(true),
                this,
                "PN may\$ put a land card\$ from his or her hand into play."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                        game.doAction(new MagicPlayCardAction(card,event.getPlayer(),MagicPlayCardAction.NONE));
                    }
                });
            }
        }
    }
]
