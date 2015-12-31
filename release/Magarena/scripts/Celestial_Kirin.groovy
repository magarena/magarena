[
    new YouCastSpiritOrArcaneTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return new MagicEvent(
                permanent,
                spell.getConvertedCost(),
                this,
                "Destroy all permanents with converted mana cost RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(
                new MagicCMCPermanentFilter(
                    PERMANENT,
                    Operator.EQUAL,
                    event.getRefInt()
                ).filter(event)
            ));
        }
    }
]
