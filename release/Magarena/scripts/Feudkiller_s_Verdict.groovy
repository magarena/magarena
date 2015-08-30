[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 10 life. Then if you have more life than an opponent, " +
                "put a 5/5 white Giant Warrior creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new ChangeLifeAction(player,10));
            if (player.getLife() > player.getOpponent().getLife()) {
                game.doAction(new PlayTokenAction(
                    player,
                    CardDefinitions.getToken("5/5 white Giant Warrior creature token")
                ));
            }
        }
    }
]
