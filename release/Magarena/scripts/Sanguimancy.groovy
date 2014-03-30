[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Draw cards and lose life equal to PN's devotion to black. ("+cardOnStack.getController().getDevotion(MagicColor.Black)+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Black);
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,amount));
            game.doAction(new MagicChangeLifeAction(player,-amount));
        }
    }
]