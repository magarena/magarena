[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_SPELL_OR_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target spell or permanent\$ to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack cardOnStack) {
                    game.doAction(new MagicRemoveItemFromStackAction(cardOnStack));
                    game.doAction(new MagicMoveCardAction(
                        cardOnStack.getCard(),
                        MagicLocationType.Stack,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
