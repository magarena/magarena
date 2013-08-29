[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return new MagicEvent(
                permanent,
                this,
                "PN searches his or her library for a land card, reveal it, and put it into his or her hand. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
            ));           
        }
    }
]