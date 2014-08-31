[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature without flying and each player, where X is the number of Beasts on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int X = game.getNrOfPermanents(MagicSubType.Beast);
            final Collection<MagicPermanent> targets = game.filterPermanents(MagicTargetFilterFactory.CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage = new MagicDamage(source,target,X);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicDamage damage = new MagicDamage(source,player,X);
                game.doAction(new MagicDealDamageAction(damage)); 
            }
        }
    }
]
