[
    new OtherSpellIsCastTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
            return spell.isInstantOrSorcerySpell();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
            return new MagicEvent(
                spell,
                new MagicMayChoice(),
                spell,
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

