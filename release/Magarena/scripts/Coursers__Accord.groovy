[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 3/3 green Centaur creature token onto the battlefield, then populate."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("3/3 green Centaur creature token")
            ));
            game.addEvent(new MagicPopulateEvent(event.getSource()));
        }
    }
]
