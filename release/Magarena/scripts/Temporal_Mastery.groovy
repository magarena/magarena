[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Take an extra turn after this one. Exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeExtraTurnsAction(event.getPlayer(),1));
            game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.Exile));
        }
    }
]
