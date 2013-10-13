[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 10 life. Then if you have more life" +
                " than an opponent, put a 5/5 white Giant Warrior creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player,10));
            final boolean more = player.getLife() > player.getOpponent().getLife();
            if (more) {
                game.doAction(new MagicPlayTokensAction(
                        player,
                        TokenCardDefinitions.get("Giant5"),
                        1
                    ));
            }
        }
    }
]
