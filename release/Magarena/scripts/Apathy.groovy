[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return upkeepPlayer == enchanted.getController() ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice("Discard a card at random?"),
                    this,
                    "PN may\$ discard a card at random. If he or she does, untap ${enchanted}"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicEvent discardEvent = MagicDiscardEvent.Random(event.getSource(), event.getPlayer())
            if (event.isYes() && discardEvent.isSatisfied()) {
                game.addEvent(discardEvent);
                game.doAction(new UntapAction(event.getPermanent().getEnchantedPermanent()))
            }
        }
    }
]
