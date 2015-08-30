MagicTargetFilter<MagicPermanent> SLIVER_CREATURE_YOU_CONTROL = Permanent("Sliver creature you control");

[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final MagicTarget target = itemOnStack.getTarget();
            return target.isPermanent() && 
                   SLIVER_CREATURE_YOU_CONTROL.accept(permanent, permanent.getController(), (MagicPermanent)target) &&
                   itemOnStack.isEnemy(permanent) ?
                new MagicEvent(
                    permanent,
                    itemOnStack,
                    this,
                    "Counter RN unless its controller pays {2}."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicCounterUnlessEvent(
                event.getSource(),
                event.getRefItemOnStack(),
                MagicManaCost.create("{2}")
            ));
        }
    }
]
