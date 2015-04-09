[
    new MagicWhenYouCastSpiritOrArcaneTrigger() {
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
            final Collection<MagicPermanent> targets=
                game.filterPermanents(
                    event.getPlayer(),
                    new MagicCMCPermanentFilter(
                        PERMANENT,
                        Operator.EQUAL,
                        event.getRefInt()
                    )
                );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
