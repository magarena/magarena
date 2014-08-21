def UNTAPPED_GOBLIN_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
    {
        return target.hasSubType(MagicSubType.Goblin) && 
               target.isUntapped() && 
               target.isController(player);
    }
};

def FIVE_UNTAPPED_GOBLIN_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(UNTAPPED_GOBLIN_YOU_CONTROL) >= 5;
    }
};

def AN_UNTAPPED_GOBLIN_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_GOBLIN_YOU_CONTROL,"an untapped Goblin you control");

[
    new MagicPermanentActivation(
        [FIVE_UNTAPPED_GOBLIN_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Untap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL)
            ];
        }

		@Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals 10 damage to each creature and each player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source=event.getPermanent();
            final int amount=10;
            final Collection<MagicPermanent> creatures=
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage=new MagicDamage(source,creature,amount);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicDamage damage=new MagicDamage(source,player,amount);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
