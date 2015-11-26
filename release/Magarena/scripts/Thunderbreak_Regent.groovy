def TARGET = Permanent("Dragon you control");
def SPELL = ItemOnStack("spell or ability an opponent controls");

[
    new BecomesTargetTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final MagicTarget target = itemOnStack.getTarget();
            return target.isPermanent() && 
                   TARGET.accept(permanent, permanent.getController(), (MagicPermanent)target) &&
                   SPELL.accept(permanent, permanent.getController(), itemOnStack) ?
                new MagicEvent(
                    permanent,
                    itemOnStack.getController(),
                    this,
                    "SN deals 3 damage to PN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getPermanent(),event.getPlayer(),3));
        }
    }
]
