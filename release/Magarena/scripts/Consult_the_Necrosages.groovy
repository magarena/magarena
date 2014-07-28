[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.TARGET_PLAYER,
                    MagicTargetChoice.TARGET_PLAYER
                ),
                0,
                this,
                "Choose one\$ - Target player draws two cards; or target player discards two cards."
            );
        }
        public MagicEvent drawTwo(final MagicSource source) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                1,
                this,
                "Target player\$ draws two cards"
            );
        }
        public MagicEvent discardTwo(final MagicSource source) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                2,
                this,
                "Target player\$ discards two cards"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefInt() == 0 && event.isMode(1)) {
                game.addEvent(drawTwo(event.getSource()));
            } else if (event.getRefInt() == 0 && event.isMode(2)) {
                game.addEvent(discardTwo(event.getSource()));
            } else if (event.getRefInt() == 1) {
               event.processTargetPlayer(game, {
                   game.doAction(new MagicDrawAction(it,2));
               }); 
            } else if (event.getRefInt() == 2) {
                event.processTargetPlayer(game, {
                    game.addEvent(new MagicDiscardEvent(event.getSource(),it,2));
                });
            }
        }
    }
]
