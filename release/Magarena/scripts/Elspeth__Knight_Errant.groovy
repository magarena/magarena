[
    new MagicPlaneswalkerActivation(1, "Token") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 1/1 white Soldier creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 white Soldier creature token")
            ));
        }
    },
    new MagicPlaneswalkerActivation(1, "Boost") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +3/+3 and gains flying until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,3,3));
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Flying));
            });
        }
    },
    new MagicPlaneswalkerActivation(-8, "Ultimate") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Artifacts, creatures, enchantments, and lands you control have indestructible.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            outerGame.doAction(new AddStaticAction(
                new MagicStatic(
                    MagicLayer.Ability,
                    ANY) {
                    @Override
                    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                        permanent.addAbility(MagicAbility.Indestructible, flags);
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return target.getController().getId() == event.getPlayer().getId() && (
                            target.isArtifact() ||
                            target.isCreature() ||
                            target.isEnchantment() ||
                            target.isLand()
                        );
                    }
                }
            ));
        }
    }
]
