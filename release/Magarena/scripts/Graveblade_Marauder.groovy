[
    new SelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "PN loses life equal to the number of creature cards in "+permanent.getController()+"'s graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer owner = event.getSource().getController();
            final MagicPlayer player = event.getPlayer();
            final int amount = CREATURE_CARD_FROM_GRAVEYARD.filter(owner).size();
            game.logAppendValue(player,amount);
            if (amount > 0) {
                game.doAction(new ChangeLifeAction(player, -amount));
            }
        }
    }
]
