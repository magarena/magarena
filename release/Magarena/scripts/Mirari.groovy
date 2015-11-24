[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) && cardOnStack.isInstantOrSorcerySpell()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{3}"))
                    ),
                    cardOnStack,
                    this,
                    "PN may\$ pay {3}. If PN does, copy RN. PN may choose new targets for the copy."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new CopyCardOnStackAction(event.getPlayer(),event.getRefCardOnStack()));
            }
        }
    }
]
