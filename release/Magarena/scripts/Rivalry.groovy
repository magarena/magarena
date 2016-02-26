[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getNrOfPermanents(MagicType.Land) > upkeepPlayer.getOpponent().getNrOfPermanents(MagicType.Land) ? new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "SN deals 2 damage to PN."
            ) : 
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
        }
    }
]
