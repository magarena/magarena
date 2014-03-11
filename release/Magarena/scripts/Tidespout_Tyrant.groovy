[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (cardOnStack.isFriend(permanent) && cardOnStack.isSpell()) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicBounceTargetPicker.create(),
                    this,
                    "Return target permanent\$ to its owner's hand."
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->
                game.doAction(new MagicRemoveFromPlayAction(target,MagicLocationType.OwnersHand));
            });
        }
    }
]
