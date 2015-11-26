[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return game.getAttackingPlayer().getNrOfAttackers()==1 ?
                new MagicEvent(
                    permanent,
                    game.getDefendingPlayer(),
                    this,
                    "PN sacrifices a creature."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                SACRIFICE_CREATURE
            ));
        }
    }
]
