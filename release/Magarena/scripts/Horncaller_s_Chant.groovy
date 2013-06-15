[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a 4/4 green Rhino creature token with trample onto the battlefield, then populate."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("Rhino4")));
            game.addEvent(new MagicPopulateEvent(event.getSource()));
        }
    }
]
