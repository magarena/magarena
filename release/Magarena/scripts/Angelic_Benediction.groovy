def EFFECT = MagicRuleEventAction.create("You may tap target creature.");

[
    //Whenever a creature you control attacks alone, you may tap target creature.
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature.isFriend(permanent) &&
                    creature.getController().getNrOfAttackers() == 1) ?
                EFFECT.getEvent(permanent):
                MagicEvent.NONE;
        }
    }
]
