[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws X cards and loses X life, where X is PN's devotion to black."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Black);
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player,amount));
            game.doAction(new ChangeLifeAction(player,-amount));
        }
    }
]
