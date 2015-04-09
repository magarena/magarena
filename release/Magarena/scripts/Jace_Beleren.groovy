[
    new MagicPlaneswalkerActivation(2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each player draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicDrawAction(player));
            }
        }
    },
    new MagicPlaneswalkerActivation(-1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_PLAYER,
                this,
                "Target player\$ draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicDrawAction(it));
            });
        }
    },
    new MagicPlaneswalkerActivation(-10) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ puts the top twenty cards of his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicMillLibraryAction(it, 20));
            });
        }
    }
]
