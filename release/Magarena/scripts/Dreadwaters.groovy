[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ puts the top X cards of his or her library into " +
                "his or her graveyard, where X is the number of lands you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = player.getNrOfPermanents(MagicType.Land);
                game.doAction(new MillLibraryAction(it,amount));
            });
        }
    }
]
