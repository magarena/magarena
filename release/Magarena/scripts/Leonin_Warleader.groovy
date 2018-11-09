[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates two 1/1 white Cat creature tokens with lifelink that are tapped and attacking"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            2.times {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 white Cat creature token with lifelink"),
                    MagicPlayMod.TAPPED_AND_ATTACKING
                ));
            }
        }
    }
]
