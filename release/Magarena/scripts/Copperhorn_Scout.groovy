[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            new MagicEvent(
                permanent,
                this,
                "PN untaps each other creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOU_CONTROL
            .except(event.getPermanent())
            .filter(event.getPlayer()) each {
                game.doAction(new UntapAction(it));
            }
        }
    }
]
