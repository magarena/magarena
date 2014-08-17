[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                    permanent,
                    this,
                    "Scry 1, then scry 2, then scry 3"
                )
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicScryXEvent(event.getSource(), event.getPlayer(), 1));
            game.addEvent(new MagicScryXEvent(event.getSource(), event.getPlayer(), 2));
            game.addEvent(new MagicScryXEvent(event.getSource(), event.getPlayer(), 3));
        }
    }
]   
