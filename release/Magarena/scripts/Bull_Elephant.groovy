def choice = new MagicTargetChoice("a Forest you control");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Return two Forests you control to their owner's hand?"),
                this,
                "PN may\$ return two Forests he controls to their owner's hands. If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(MagicSubType.Forest) >=2 && event.isYes()) {
                game.addEvent(new MagicBounceChosenPermanentEvent(
                    event.getSource(), 
                    choice
                ));		game.addEvent(new MagicBounceChosenPermanentEvent(
                    event.getSource(), 
                    choice
                ));            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
