[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                new MagicMayChoice("Pay 2 life?"),
                this,
                "PN may\$ pay 2 life. If PN doesn't, PN returns a permanent he or she controls to its owner's hand."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
            } else {
                game.addEvent(new MagicBounceChosenPermanentEvent(
                    event.getSource(), 
                    event.getPlayer(), 
                    A_PERMANENT_YOU_CONTROL
                ));
            }
        }
    }
]
