[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ draws a card. "+
                "PN gains 7 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicDrawAction(it));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),7));
            });
        }
    }
]
