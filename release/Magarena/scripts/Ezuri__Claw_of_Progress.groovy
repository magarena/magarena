def choice = MagicTargetChoice.Positive("another target creature you control")

[
    new AtBeginOfCombatTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer turnPlayer) {
            return permanent.getController().getExperience() > 0;
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.getController() == attackingPlayer ?
                new MagicEvent(
                    permanent,
                    choice,
                    this,
                    "PN puts X +1/+1 counters on another target creature he or she control\$, where X is the number of experience counters PN have."
                ):
                MagicEvent.NONE;
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
