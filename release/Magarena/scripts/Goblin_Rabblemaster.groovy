def EFFECT = MagicRuleEventAction.create("Put a 1/1 red Goblin creature token with haste onto the battlefield.");

[
    new MagicAtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.isController(attackingPlayer) ? 
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    },
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +1/+0 until end of turn for each other attacking Goblin."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent it = event.getPermanent();
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(MagicTargetFilterFactory.ATTACKING_GOBLIN, it);
            final int amt = game.filterPermanents(filter).size();
            game.doAction(new MagicChangeTurnPTAction(it,amt,0));
        }
    }
]
