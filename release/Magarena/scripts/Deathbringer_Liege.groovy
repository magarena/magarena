[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    cardOnStack.hasColor(MagicColor.Black)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        NEG_TARGET_CREATURE
                    ),
                    MagicDestroyTargetPicker.Destroy,
                    this,
                    "PN may\$ destroy target creature\$ if it's tapped."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    if (it.isTapped()) {
                        game.doAction(new DestroyAction(it));
                    }
                });
            }
        }
    }
]
