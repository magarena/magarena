[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{U}{U}{B}{B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ loses X life and puts the top X cards of his or her library into his or her graveyard, " +
                "where X is the number of Zombies you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Zombie);
                game.doAction(new ChangeLifeAction(it,-amount));
                game.doAction(new MillLibraryAction(it,amount));
            });
        }
    }
]
