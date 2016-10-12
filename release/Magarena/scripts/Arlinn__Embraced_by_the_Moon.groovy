def tapDealsDamage = MagicPermanentActivation.create("{T}: SN deals damage equal to its power to target creature or player.");

[
    new MagicPlaneswalkerActivation(-6) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Creatures you control have haste and " +
                "'{T}: This creature deals damage equal to its power to target creature or player.'\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final long pId = outerEvent.getPlayer().getId();
            outerGame.doAction(new AddStaticAction(
                new MagicStatic(MagicLayer.Ability, ANY) {
                    @Override
                    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                        permanent.addAbility(MagicAbility.Haste, flags);
                        permanent.addAbility(tapDealsDamage);
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return target.getController().getId() == pId && target.isCreature();
                    }
                }
            ));
        }
    }
]
