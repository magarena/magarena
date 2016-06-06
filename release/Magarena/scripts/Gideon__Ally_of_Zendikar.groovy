[
    new MagicPlaneswalkerActivation(-4) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Creatures you control get +1/+1.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final long pId = outerEvent.getPlayer().getId();
            outerGame.doAction(new AddStaticAction(
                new MagicStatic(MagicLayer.ModPT, ANY) {
                    @Override
                    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                        pt.add(1, 1);
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
