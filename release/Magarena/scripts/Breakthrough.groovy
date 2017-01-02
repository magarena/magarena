[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws 4 cards, then chooses X cards in his or her hand and discard the rest."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player, 4));
            final int keep = event.getCardOnStack().getX();
            game.logAppendX(player, keep);
            final int amount = player.getHandSize() - keep;
            if (amount > 0) {
                game.addEvent(new MagicDiscardEvent(event.getSource(), player, amount));
            }
        }
    }
]
