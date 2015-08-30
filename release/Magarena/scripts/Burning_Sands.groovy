[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return died.hasType(MagicType.Creature) ?
                new MagicEvent(
                    permanent,
                    died.getController(),
                    this,
                    "PN sacrifices a land."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),event.getPlayer(),SACRIFICE_LAND));
        }
    }
]
