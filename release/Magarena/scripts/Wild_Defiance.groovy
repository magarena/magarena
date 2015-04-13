[
    new MagicWhenTargetedTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isInstantOrSorcerySpell();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                permanent.getController(),
                CREATURE_YOU_CONTROL
            );
            for (final MagicPermanent perm : targets) {
                if (itemOnStack.containsInChoiceResults(perm)) {
                    return new MagicEvent(
                        permanent,
                        perm,
                        this,
                        "RN gets +3/+3 until end of turn."
                    );
                }
            }
            return MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getRefPermanent(),3,3));
        }
    }
]
