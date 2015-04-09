[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}{U}"),
                new MagicExileCardEvent(source, TARGET_CREATURE_CARD_FROM_GRAVEYARD)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 2/2 black Zombie creature token onto the battlefield, " +
                "then put a +1/+1 counter on each Zombie creature you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicPlayTokenAction(
                player,
                TokenCardDefinitions.get("2/2 black Zombie creature token")
            ));
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    player,
                    ZOMBIE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeCountersAction(target,MagicCounterType.PlusOne,1));
            }
        }
    }
]
