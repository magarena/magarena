[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            new MagicEvent(
                permanent,
                this,
                "Each other attacking creature gains fear until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            ATTACKING_CREATURE
            .except(event.getPermanent())
            .filter(game) each {
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Fear));
            }
        }
    }
]
