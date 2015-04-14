[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                payedCost.getX(),
                this,
                "Choose one\$ - each player draws X cards; " +
                "or each player puts the top X cards of his or her library into his or her graveyard.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getRefInt();
            if (event.isMode(1)) {
                for (final MagicPlayer player : game.getAPNAP()) {
                    game.doAction(new DrawAction(player,X));
                }    
            } else if (event.isMode(2)) {
                for (final MagicPlayer player : game.getAPNAP()) {
                    game.doAction(new MagicMillLibraryAction(player,X));
                }
            }
        }
    }
]
