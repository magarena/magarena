[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Life"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Exchange target opponent's\$ life total with SN's toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPermanent permanent = event.getPermanent();
                final int life = it.getLife();
                final int toughness = permanent.getToughness();
                // exchange life with toughness even when they are equal
                // because toughness can be modified in layer ModPT (7c)
                game.doAction(new ChangeLifeAction(it, toughness - life));
                game.doAction(new AddStaticAction(permanent,
                    new MagicStatic(MagicLayer.SetPT) {
                    @Override
                    public void modPowerToughness(final MagicPermanent S, final MagicPermanent P, final MagicPowerToughness pt) {
                        pt.setToughness(life);
                    }
                }));
            })
        }
    }
]
