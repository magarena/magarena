def choice = MagicTargetChoice.Positive("another target creature you control")

[
    new AtBeginOfCombatTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return (permanent.getController().getExperience() > 0) && (permanent.getController() == attackingPlayer);
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return new MagicEvent(
                permanent,
                choice,
                this,
                "PN puts X +1/+1 counters on another target creature he or she control\$, where X is the number of experience counters PN has."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = event.getPlayer().getExperience();
                game.logAppendX(event.getPlayer(), X);
                game.doAction(new ChangeCountersAction(event.getPlayer(), it, MagicCounterType.PlusOne, X));
            });
        }
    }
]
