def effect = MagicRuleEventAction.create("You may draw a card.");

[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (died.hasType(MagicType.Artifact) && permanent.isOpponent(died.getOwner())) ?
                effect.getEvent(permanent) : MagicEvent.NONE;
        }
    }
]
