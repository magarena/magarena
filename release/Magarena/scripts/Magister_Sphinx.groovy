[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$'s life total becomes 10."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final int amount = 10 - player.getLife();
                    game.doAction(new MagicChangeLifeAction(player,amount));
                }
            });
        }
    }    
]
