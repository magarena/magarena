[
    new BecomesTargetTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return itemOnStack.getTarget() == enchanted ?
                new MagicEvent(
                    permanent,
                    itemOnStack.getController(),
                    enchanted,
                    this,
                    "PN gains control of RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainControlAction(event.getPlayer(), event.getRefPermanent(), MagicStatic.Forever));
        }
    }
]
