[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put three 1/1 white Soldier creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 white Soldier creature token"),
                3
            ));
        }
    },
    new MagicPlaneswalkerActivation(-3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy all creatures with power 4 or greater."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(
                game.filterPermanents(CREATURE_POWER_4_OR_MORE)
            ));
        }
    },
    new MagicPlaneswalkerActivation(-7) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Creatures you control get +2/+2 and have flying.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            outerGame.doAction(new AddStaticAction(
                new MagicStatic(
                    MagicLayer.ModPT,
                    ANY
                ) {
                    @Override
                    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                        pt.add(2, 2);
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return target.getController().getId() == event.getPlayer().getId() && target.isCreature();
                    }
                }
            ));
            outerGame.doAction(new AddStaticAction(
                new MagicStatic(
                    MagicLayer.Ability,
                    ANY
                ) {
                    @Override
                    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                        permanent.addAbility(MagicAbility.Flying, flags);
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return target.getController().getId() == event.getPlayer().getId() && target.isCreature();
                    }
                }
            ));
        }
    }
]
