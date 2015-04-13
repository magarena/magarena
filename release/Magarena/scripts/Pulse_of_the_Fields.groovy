[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 4 life. Then if your opponent " +
                "has more life than you, return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player,4));
            final boolean more = player.getOpponent().getLife() > player.getLife();
            if (more) {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersHand));
            }
        }
    }
]
