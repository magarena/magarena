def SLIVER_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.hasSubType(MagicSubType.Sliver);
        }
    };

[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                permanent.getController(),
                SLIVER_CREATURE_YOU_CONTROL
            );
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
        public void executeEvent(final MagicGame game, final MagicEvent mevent) {
            final MagicSource source = mevent.getSource();
            final MagicItemOnStack target = mevent.getRefItemOnStack();
            game.addEvent(new MagicCounterUnlessEvent(source,target,MagicManaCost.create("{2}")));
        }

    }
]
