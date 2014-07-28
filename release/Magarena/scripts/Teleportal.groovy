[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target creature\$ you control gets +1/+0 until end of turn and can't be blocked this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it, 1, 0));
                game.doAction(new MagicGainAbilityAction(it, MagicAbility.Unblockable));
            });
        }
    },
    new MagicOverloadActivation(MagicTiming.Pump) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{U}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each creature you control gets +1/+0 until end of turn and can't be blocked this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature, 1, 0));
                game.doAction(new MagicGainAbilityAction(creature, MagicAbility.Unblockable));
            }
        }
    }
]
