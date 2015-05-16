def filter = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
        return target.hasType(MagicType.Creature) && target.getConvertedCost() <= 1;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Graveyard;
    }
};

def choice = new MagicTargetChoice(filter, "a creature card with converted mana cost 1 or less from your graveyard");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    TARGET_CREATURE_YOU_CONTROL,
                    NEG_TARGET_CREATURE,
                    choice
                ),
                this,
                "Choose one — (1) Return target creature you control and all Auras you control attached to it to their owner's hand. (2) Destroy target creature and you lose life equal to its toughness. (3) Return target creature card with converted mana cost 1 or less from your graveyard to the battlefield." 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                    for (final MagicPermanent aura : creature.getAuraPermanents()) {
                        game.doAction(new RemoveFromPlayAction(aura, MagicLocationType.OwnersHand));
                    }
                    game.doAction(new RemoveFromPlayAction(creature, MagicLocationType.OwnersHand));
                });
            } else if (event.isMode(2)) {
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                    final int amount = creature.getToughness();
                    final MagicPlayer player = event.getPlayer();
                    game.logAppendMessage(player,"("+amount+")");
                    game.doAction(new DestroyAction(creature));
                    game.doAction(new ChangeLifeAction(player,-creature.getToughness()));
                });
            } else if (event.isMode(3)) {
                event.processTargetCard(game, {
                    game.doAction(new ReanimateAction(it,event.getPlayer()));
                });
            }
        }
    }
]
