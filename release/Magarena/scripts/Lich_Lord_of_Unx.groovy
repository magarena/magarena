[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{U}{U}{B}{B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
         	return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ puts X cards from the top of his or her library into his or her graveyard " +
				"and loses X life where X is the number of Zombies you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.ZOMBIE_YOU_CONTROL);
				int amount = targets.size();
                game.doAction(new MagicMillLibraryAction(player,amount));
                game.doAction(new MagicChangeLifeAction(player,-amount));
            });
        }
    }
]
