[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasColor(MagicColor.Blue) && cardOnStack.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.UNTAP,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ untap SN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicUntapAction(event.getPermanent()));
        }
    }
]
