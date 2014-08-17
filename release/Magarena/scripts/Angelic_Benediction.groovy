def sourceEvent = MagicRuleEventAction.create("You may tap target creature.");

[
    //Whenever a creature you control attacks alone, you may tap target creature.
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature.isFriend(permanent) &&
                    creature.getController().getNrOfAttackers() == 1) ?
                sourceEvent.getEvent(permanent):
                MagicEvent.NONE;
        }
    }
]
