def effect = MagicRuleEventAction.create("You may draw a card.");

[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isAttacking() &&
                    (otherPermanent.hasSubType(MagicSubType.Elf) || otherPermanent.hasSubType(MagicSubType.Kithkin)) &&
                    otherPermanent.isOwner(permanent.getController())) ?
                effect.getEvent(permanent) : MagicEvent.NONE;
        }
    }
]
