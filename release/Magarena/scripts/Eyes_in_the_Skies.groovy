[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a 1/1 white Bird creature token with flying onto the battlefield, then populate."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("Bird1")));
            game.addEvent(new MagicPopulateEvent(event.getSource()));
        }
    }
]
