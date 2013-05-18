[
    new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            final MagicPlayer player = permanent.getController();
            final int amount = lifeChange.amount;
            return (player == lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    player,
                    amount,
                    this,
                    player.getOpponent() + " loses " + amount + " life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(
                event.getPlayer().getOpponent(),
                -event.getRefInt()
            ));
        }
    }
]
