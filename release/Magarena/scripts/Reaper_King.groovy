[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Scarecrow)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_PERMANENT,
                    MagicDestroyTargetPicker.Destroy,
                    this,
                    "Destroy target permanent\$."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
            });
        }
    }
]
