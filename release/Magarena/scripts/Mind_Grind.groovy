[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getOpponent(),
                payedCost.getX(),
                this,
                "PN reveals cards from the top of his or her library until he or she reveals RN land cards, " +
                "then puts all cards revealed this way into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MillLibraryUntilAction(player, MagicType.Land, amount));
        }
    }
]
