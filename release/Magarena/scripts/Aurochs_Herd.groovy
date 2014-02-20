[
    new MagicWhenComesIntoPlayTrigger() {
       @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                this,
                "PN may search PN's library for an Aurochs card, reveal it, and put it into PN's hand. If PN does, shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                new MagicMayChoice(
                    "Search for an Aurochs card?",
                    new MagicTargetChoice("an Aurochs card from your library")
                )
            ));
        }
    }
]
