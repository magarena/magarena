[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws X cards and loses X life where X is PN's devotion to black."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int devotionBlack = player.getDevotion(MagicColor.Black);
            if (devotionBlack>0) {
                game.doAction(new MagicDrawAction(player,devotionBlack));
                game.doAction(new MagicChangeLifeAction(player,devotionBlack));
            }
        }
    }
]
