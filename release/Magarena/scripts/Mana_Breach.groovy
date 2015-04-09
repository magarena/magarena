[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack.getController(),
                A_LAND_YOU_CONTROL,
                MagicBounceTargetPicker.create(),
                this,
                "PN returns a land PN controls\$ to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            });
        }
    }
]
