[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player,
                this,
                "PN sacrifices a non-Zombie creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                player,
                MagicTargetChoice.SACRIFICE_NON_ZOMBIE
            ));
        }
    },
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(permanent.getController(),MagicTargetFilterFactory.CREATURE);
            return (targets.size() == 0) ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN."):
                MagicEvent.NONE;
        }
    }
]
