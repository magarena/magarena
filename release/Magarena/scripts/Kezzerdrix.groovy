[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getOpponent().controlsPermanent(MagicType.Creature) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals 4 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getOpponent().controlsPermanent(MagicType.Creature) == false) {
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),4));
            }
        }
    }
]
