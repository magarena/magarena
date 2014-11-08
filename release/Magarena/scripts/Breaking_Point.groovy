[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {   
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getOpponent(),
                new MagicMayChoice("Have SN deal 6 damage to you?"),
                this,
                "PN may\$ have SN deal 6 damage to him or her. If PN doesn't, destroy all creatures."
                + " Creatures destroyed this way can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicDealDamageAction(event.getCardOnStack(),event.getCardOnStack().getOpponent(),6));
            } else {
                 final Collection<MagicPermanent> targets = game.filterPermanents(MagicTargetFilterFactory.CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(MagicChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
            }
            final MagicDestroyAction destroy = new MagicDestroyAction(targets);         
                game.doAction(destroy); 
            }
        }
    }
]
