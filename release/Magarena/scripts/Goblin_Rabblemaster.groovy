def EFFECT = MagicRuleEventAction.create("Put a 1/1 red Goblin creature token with haste onto the battlefield.");

[
    new MagicAtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.isController(attackingPlayer) ? 
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
