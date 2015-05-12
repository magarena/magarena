[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer defender = damage.getTargetPlayer();
            return defender == game.getDefendingPlayer() ?
                new MagicEvent(
                    permanent,
                    defender,
                    this,
                    "SN deals damage equal to its power to each blocking creature."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            BLOCKING_CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(),it,event.getPermanent().getPower()));
            }
        }
    }
]
