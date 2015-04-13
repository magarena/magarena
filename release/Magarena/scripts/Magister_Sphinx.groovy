[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return new MagicEvent(
                permanent,
                TARGET_PLAYER,
                this,
                "Target player\$'s life total becomes 10."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = 10 - it.getLife();
                game.doAction(new ChangeLifeAction(it,amount));
            });
        }
    }    
]
