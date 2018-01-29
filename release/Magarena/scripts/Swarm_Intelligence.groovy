[
    new OtherSpellIsCastTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return cardOnStack.isInstantOrSorcerySpell();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(),
                cardOnStack,
                this,
                "PN may\$ copy RN. PN may choose new targets for the copy."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new CopyCardOnStackAction(event.getPlayer(), event.getRefCardOnStack()));
            }
        }
    }
]

