[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "{R}{R}"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{R}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target creature you control\$ can't be blocked this turn. Destroy it and SN at end of combat."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainAbilityAction(it, MagicAbility.Unblockable));
                game.doAction(new AddTurnTriggerAction(
                    it,
                    AtEndOfCombatTrigger.Destroy
                ));
                game.doAction(new AddTurnTriggerAction(
                    event.getPermanent(),
                    AtEndOfCombatTrigger.Destroy
                ))
            });
        }
    },

    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "{R}{R}{R}{R}"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{R}{R}{R}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target creature you control\$ can't be blocked this turn. Destroy it at end of combat."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainAbilityAction(it, MagicAbility.Unblockable));
                game.doAction(new AddTurnTriggerAction(
                    it,
                    AtEndOfCombatTrigger.Destroy
                ));
            });
        }
    }
]
