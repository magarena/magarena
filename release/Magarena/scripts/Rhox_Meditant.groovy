[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.getController().controlsPermanent(MagicColor.Green) == true ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls a green permanent, draw a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicColor.Green) == true) {
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }
    }
]
