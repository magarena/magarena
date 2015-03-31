[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature, where X is the number of creatures on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=game.getNrOfPermanents(MagicType.Creature);
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicDealDamageAction(event.getSource(),target,amount));
            }
        }
    }
]
