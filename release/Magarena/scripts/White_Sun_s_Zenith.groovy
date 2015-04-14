[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts X 2/2 white Cat creature tokens onto the battlefield for each Forest PN controls. "+
                "Shuffle SN into its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardOnStack spell = event.getCardOnStack();
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("2/2 white Cat creature token"),
                spell.getX()
            ));
            game.doAction(new ChangeCardDestinationAction(spell,MagicLocationType.OwnersLibrary));
        }
    }
]
