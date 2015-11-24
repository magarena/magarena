[
    new SpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack spell) {
            return new MagicEvent(
                spell,
                this,
                "PN takes an extra turn after this one."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeExtraTurnsAction(event.getPlayer(),1));
        }
    }
]
