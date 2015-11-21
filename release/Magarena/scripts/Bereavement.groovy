[
    new WhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return died.hasType(MagicType.Creature) && died.hasColor(MagicColor.Green) ?
                new MagicEvent(
                    permanent,
                    died.getController(),
                    this,
                    "PN discards a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer()));
        }
    }
]
