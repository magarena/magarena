[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature with flying where X is PN's devotion to green."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int amount = event.getCardOnStack().getController().getDevotion(MagicColor.Green);
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITH_FLYING);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(source,target,amount);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
