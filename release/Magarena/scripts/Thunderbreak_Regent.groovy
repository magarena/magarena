MagicTargetFilter<MagicPermanent> DRAGON_YOU_CONTROL = Permanent("Dragon you control");

[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final MagicTarget target = itemOnStack.getTarget();
            return target.isPermanent() &&
                   DRAGON_YOU_CONTROL.accept(permanent, permanent.getController(), (MagicPermanent)target) &&
                   itemOnStack.getController() == permanent.getOpponent() ?
                new MagicEvent(
                    permanent,
                    itemOnStack.getController(),
                    this,
                    "SN deals 3 damage to RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getPermanent(),event.getRefPlayer(),3));
        }
    }
]
