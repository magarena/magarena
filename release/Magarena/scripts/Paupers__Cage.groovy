[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return (permanent.isOpponent(player) &&
                    player.getHandSize() <= 2) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN deals 2 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
        }
    }
]
