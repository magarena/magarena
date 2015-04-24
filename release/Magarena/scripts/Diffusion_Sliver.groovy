MagicTargetFilter<MagicPermanent> SLIVER_CREATURE_YOU_CONTROL = Permanent("Sliver creature you control");

[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final Collection<MagicPermanent> targets = permanent.getController().filterPermanents(SLIVER_CREATURE_YOU_CONTROL);
            for (final MagicPermanent perm : targets) {
                if (itemOnStack.containsInChoiceResults(perm) && perm.isEnemy(itemOnStack)) {
                    return new MagicEvent(
                        perm,
                        itemOnStack,
                        this,
                        "Counter spell or ability\$ unless its controller pays {2}."
                    );
                }
            }
            return MagicEvent.NONE;
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
