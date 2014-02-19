[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (cardOnStack.isFriend(permanent) && cardOnStack.hasType(MagicType.Artifact)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTapTargetPicker.Untap,
                    this,
                    "PN untaps target creature\$"
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicUntapAction(creature));
            });
        }
    }
]
