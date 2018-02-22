[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{B}{B}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                this,
                "Destroy target creature\$. " +
                "If that creature dies this way, PN creates a black Vampire creature token with that creature's power and toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int power = it.getPower();
                final int toughness = it.getToughness();
                final DestroyAction act = new DestroyAction(it);
                game.doAction(act);
                if (act.isDestroyed() && it.getCard().isInGraveyard()) {
                    game.doAction(new PlayTokenAction(event.getPlayer(), CardDefinitions.getToken(power, toughness, "black Vampire creature token")));
                }
            });
        }
    }
]

