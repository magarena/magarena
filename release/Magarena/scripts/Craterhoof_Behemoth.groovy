[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Creatures PN controls gain trample " +
                "and get +X/+X until end of turn, where X is " +
                "the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    CREATURE_YOU_CONTROL);
            final int amount = targets.size();
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Trample));
                game.doAction(new MagicChangeTurnPTAction(creature,amount,amount));
            }
        }
    }
]
