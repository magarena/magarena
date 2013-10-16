[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source,"{X}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                amount,
                this,
                "Target player\$ puts the top "+amount+" cards of his or her library " +
                "into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final int amount = event.getRefInt();
                    if (amount > 0) {
                        game.doAction(new MagicMillLibraryAction(player,amount));
                    }
                }
            });
        }
    }
]
