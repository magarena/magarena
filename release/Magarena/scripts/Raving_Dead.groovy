[ 
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "PN loses half his or her life, rounded down."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.getLife()>0) {
                game.doAction(new ChangeLifeAction(player, -player.getLife()/2));
            }
        }
    }
]
