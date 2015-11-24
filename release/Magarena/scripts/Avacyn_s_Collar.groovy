def effect = MagicRuleEventAction.create("Put a 1/1 white Spirit creature token with flying onto the battlefield.")

[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final MagicPermanent equipped = permanent.getEquippedCreature();
            return (equipped == died && equipped.hasSubType(MagicSubType.Human)) ?
                effect.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
