[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
       public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Target opponent\$ puts a card from his or her hand on top of his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.getPermanent().getOpponent().getHandSize() >=1) {
        event.processTargetPlayer(game, {
            game.addEvent(new MagicReturnCardEvent(event.getSource(), it));
            });
            }
        }
    }
]
