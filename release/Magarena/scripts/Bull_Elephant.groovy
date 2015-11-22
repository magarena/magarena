def choice = new MagicTargetChoice("a Forest you control");

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Return two Forests you control to their owner's hand?"),
                this,
                "PN may\$ return two Forests he or she controls to their owner's hand. If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = new MagicRepeatedPermanentsEvent(event.getSource(), choice, 2, MagicChainEventFactory.Bounce);
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
