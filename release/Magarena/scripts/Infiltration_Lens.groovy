def effect = MagicRuleEventAction.create("You may draw two cards.");

[
    new BlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.getEquippedCreature() == creature.getBlockedCreature()) ?
                effect.getEvent(permanent) : MagicEvent.NONE;
        }
    }
]
