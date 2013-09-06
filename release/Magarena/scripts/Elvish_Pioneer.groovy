[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.BASIC_LAND_CARD_FROM_HAND
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ put a basic land card\$ from his or her hand into play tapped."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                        final MagicPlayCardAction action = new MagicPlayCardAction(card,event.getPlayer());
                        game.doAction(action);
                        game.doAction(new MagicTapAction(action.getPermanent(),false));
                    }
                });
            }
        }
    }
]
