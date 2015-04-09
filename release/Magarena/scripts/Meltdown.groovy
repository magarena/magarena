[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getX(),
                this,
                "Destroy each artifact with converted mana cost RN or less."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(
                    new MagicCMCPermanentFilter(
                        ARTIFACT,
                        Operator.LESS_THAN_OR_EQUAL,
                        event.getRefInt()
                    )
                );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
