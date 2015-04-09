def CREATURE_CMC_LEQ_3 = new MagicCMCPermanentFilter(
    CREATURE,
    Operator.LESS_THAN_OR_EQUAL,
    3
);

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
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),CREATURE_CMC_LEQ_3);
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
