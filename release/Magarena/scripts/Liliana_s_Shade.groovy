[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ search PN's library for a Swamp card, reveal it, put it into PN's hand, then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchIntoHandEvent(
                    event,
                    new MagicTargetChoice("a Swamp card from your library")
                ));
            }
        }
    }
]
