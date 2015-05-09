[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Put "+x+" 1/1 red Goblin creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            int x = event.getCardOnStack().getX();
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("1/1 red Goblin creature token"),
                x
            ));
        }
    }
]
