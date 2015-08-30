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
            game.doAction(new DestroyAction(
                new MagicCMCPermanentFilter(
                    ARTIFACT,
                    Operator.LESS_THAN_OR_EQUAL,
                    event.getRefInt()
                ).filter(event)
            ));
        }
    }
]
