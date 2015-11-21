[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Return land you control to its owner's hand?"),
                this,
                "PN may\$ return a land PN controls to its owner's hand. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent cost = new MagicBounceChosenPermanentEvent(
                event.getSource(), 
                event.getPlayer(), 
                new MagicTargetChoice("a land you control")
            );
            if (event.isYes() && cost.isSatisfied()) {
                game.addEvent(cost);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
