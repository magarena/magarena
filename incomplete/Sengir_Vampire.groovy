[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (target.isCreature() && damage.getSource() == permanent) {
                //add turn trigger to SN to activate when creatures dies
                //will cause multiple triggers if SN dealt damage to creature multiple times in a single turn
            }
            return MagicEvent.NONE; 
        }
        @Override       
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
            game.doAction(new MagicDestroyAction(creature));
        }
    }
]
