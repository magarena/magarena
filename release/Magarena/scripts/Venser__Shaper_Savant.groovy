[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_SPELL_OR_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target spell or permanent\$ to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicRemoveItemFromStackAction(it));
                game.doAction(new MoveCardAction(
                    it.getCard(),
                    MagicLocationType.Stack,
                    MagicLocationType.OwnersHand
                ));
            });
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            });
        }
    }
]
