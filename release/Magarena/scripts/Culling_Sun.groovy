
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy each creature with converted mana cost 3 or less."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(
                new MagicCMCPermanentFilter(
                    CREATURE,
                    Operator.LESS_THAN_OR_EQUAL,
                    3
                ).filter(event)
            ));
        }
    }
]
