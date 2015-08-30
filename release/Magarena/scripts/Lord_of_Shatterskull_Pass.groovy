[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(6,6);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.hasCounters(MagicCounterType.Level);
        }
    },
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent.getCounters(MagicCounterType.Level) >= 6 ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals 6 damage to each creature defending player controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOU_CONTROL.filter(game.getDefendingPlayer()) each {
                game.doAction(new DealDamageAction(event.getSource(),it,6));
            }
        }
    }
]
