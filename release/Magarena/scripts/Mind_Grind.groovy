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
            final MagicCardList library = player.getLibrary();
            int landCards = 0;
            while (landCards < amount && library.size() > 0) {
                if (library.getCardAtTop().hasType(MagicType.Land)) {
                    landCards++;
                }
                game.doAction(new MillLibraryAction(player,1));
            }
        }
    }
]
