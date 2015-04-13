[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_PLAYER,
                this,
                "Target player\$ takes an extra turn after this one. "+
                "Shuffle SN into its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicChangeExtraTurnsAction(it,1));
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersLibrary));
            });
        }
    }
]
