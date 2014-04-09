[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$ puts the top four cards of his or her library into his or her graveyard. PN draws a card for each creature card put into that graveyard this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                game.doAction(new MagicMillLibraryAction(player,4));
                for(int i=0;i<4;i++) {
                    if(player.getGraveyard().size-i > 0) {
                        if(player.getGraveyard().get(player.getGraveyard().size-1-i).hasType(MagicType.Creature)) {
                          game.doAction(new MagicDrawAction(event.getSource().getController()));
                        }
                    }
                }
                MagicCard.NONE;

            });
        }
    }
]
