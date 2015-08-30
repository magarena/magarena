[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 2 damage to target creature.\$ If PN controls a black permanent, destroy the creature instead."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (event.getPlayer().getNrOfPermanents(MagicColor.Black) > 0) {
                    game.doAction(new DestroyAction(it));
                } else {
                    game.doAction(new DealDamageAction(event.getSource(), it, 2));
                }
            });
        }
    }
]
