[
    new MagicBattalionTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "Creatures PN controls gain first strike and trample until end of turn and can't be blocked by creature tokens this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> creatures=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.FirstStrike));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Trample));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.CannotBeBlockedByTokens));
            }
        }
    }
]
