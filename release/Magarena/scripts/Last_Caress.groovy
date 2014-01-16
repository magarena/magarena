[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ loses 1 life and PN gains 1 life. PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                game.doAction(new MagicChangeLifeAction(player,-1));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
