[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "If you have less life than an opponent, you gain 6 life. " +
                "If you control fewer creatures than an opponent, " +
                "put three 1/1 white Soldier creature tokens onto the battlefield."
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
                    TokenCardDefinitions.get("1/1 white Soldier creature token"),
                    3
                ));
            }
        }
    }
]
