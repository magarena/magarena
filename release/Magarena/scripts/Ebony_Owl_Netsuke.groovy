[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return (permanent.isOpponent(player) &&
                    player.getHandSize() >= 7) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN deals 4 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),4));
        }
    }
]
