[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getOpponent(),
                this,
                "PN reveals cards from the top of his or her library until he or she reveals X land cards, then puts all cards revealed this way into his or her graveyard. X can't be 0."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            final MagicPlayer player = event.getPlayer();
            for(int i = 0; i < amount; ) {
                if(player.getLibrary().size() <= 0){ i=amount; }
                else {
                    if(player.getLibrary().getCardAtTop().hasType(MagicType.Land)){ i++; }
                    game.doAction(new MagicMillLibraryAction(player,1));
                }
            }
        }
    }
]
