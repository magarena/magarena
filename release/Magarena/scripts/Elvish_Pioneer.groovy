[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.TARGET_BASIC_LAND_CARD_FROM_HAND
                ),
                new MagicGraveyardTargetPicker(true),
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
                        final MagicPlayCardAction action = new MagicPlayCardAction(card,event.getPlayer(),MagicPlayCardAction.NONE);
                        game.doAction(action);
                        game.doAction(new MagicTapAction(action.getPermanent(),false));
                    }
                });
            }
        }
    }
]
