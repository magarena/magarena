[
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
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final long pId = outerEvent.getPlayer().getId();
            outerGame.doAction(new AddStaticAction(
                new MagicStatic(MagicLayer.Ability, ANY) {
                    @Override
                    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                        permanent.addAbility(MagicAbility.Indestructible, flags);
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return target.getController().getId() == pId && (
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
