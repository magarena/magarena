[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController() == cardOnStack.getGame().getTurnPlayer() && cardOnStack.getGame().isMainPhase() ? 3 : 2;
            return new MagicEvent(
                cardOnStack,
                amount,
                this,
                "SN deals 2 damage to each creature and each player. If PN cast this spell during his or her main phase, SN deals 3 damage to each creature and each player instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            game.logAppendValue(event.getPlayer(), amount);
            CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            }
            PLAYER.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            }
        }
    }
]
