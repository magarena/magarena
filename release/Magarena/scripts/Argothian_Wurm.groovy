[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(MagicTargetChoice.SACRIFICE_LAND),
                this,
                "PN may\$ sacrifice a land\$. If a he or she does, put SN on the top of its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicSacrificeAction(it));
                    game.doAction(new MagicRemoveFromPlayAction(
                        event.getPermanent(),
                        MagicLocationType.TopOfOwnersLibrary
                    ));
                });
            } 
        }
    }
]
