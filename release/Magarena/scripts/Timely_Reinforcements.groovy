[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "If PN has less life than an opponent, PN gains 6 life. " +
                "If PN controls fewer creatures than an opponent, " +
                "PN creates three 1/1 white Soldier creature tokens."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.getLife() < player.getOpponent().getLife()) {
                game.doAction(new ChangeLifeAction(player,6));
            }
            if (player.getNrOfPermanents(MagicType.Creature) <
                player.getOpponent().getNrOfPermanents(MagicType.Creature)) {
                game.doAction(new PlayTokensAction(
                    player,
                    CardDefinitions.getToken("1/1 white Soldier creature token"),
                    3
                ));
            }
        }
    }
]
