[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN sacrifices a creature. " +
                "If PN can't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent cost = new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                SACRIFICE_CREATURE
            );
            if (cost.isSatisfied()) {
                game.addEvent(cost);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]

