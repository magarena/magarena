[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player sacrifices a land for each card in PN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getHandSize();
            for (final MagicPlayer player : game.getAPNAP()) {
                game.logAppendMessage(event.getPlayer(),player.getName()+" sacrifices "+amount+" land.")
                for (int i=amount;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        SACRIFICE_LAND
                    ));
                }  
            }
        }
    }
]
