[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ puts the top half of his or her library, rounded down, into " +
                "his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                def amount = it.getLibrary().size().intdiv(2);
                game.doAction(new MillLibraryAction(it,amount));
            });
        }
    }
]
