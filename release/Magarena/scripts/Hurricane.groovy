[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature with flying and each player. (X="+amount+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int amount = event.getCardOnStack().getX();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_WITH_FLYING);
            for (final MagicPermanent target : targets) {
                game.doAction(new DealDamageAction(source,target,amount));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DealDamageAction(source,player,amount));
            }
        }
    }
]
