[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Target player\$ puts the top card of his or her library " +
                "into his or her graveyard. SN gets +X/+X " +
                "until end of turn, where X is that card's converted mana cost"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicMillLibraryAction act = new MagicMillLibraryAction(it, 1);
                game.doAction(act);
                for (final MagicCard card : act.getMilledCards()) {
                    final int amount = card.getConvertedCost();
                    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),amount,amount));
                }
            });
        }
    }
]
