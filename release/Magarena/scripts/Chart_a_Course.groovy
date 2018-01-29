[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws two cards. Then PN discards a card unless PN attacked with a creature this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player, 2));
            if (player.getCreaturesAttackedThisTurn() < 1) {
                game.addEvent(new MagicDiscardEvent(event.getSource(), player, 1));
            }
        }
    }
]

