[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals damage to target creature or player\$ equal to the number of Arcane cards in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = card(MagicSubType.Arcane).from(MagicTargetType.Graveyard).filter(event).size();
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
