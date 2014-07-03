[
    new MagicChannelActivation("{1}{B}", new MagicActivationHints(MagicTiming.Attack, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicNoCombatTargetPicker(false,true,false),
                this,
                "Target creature\$ can't block this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.CannotBlock));
            });
        }
    }
]
