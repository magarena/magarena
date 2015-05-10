[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 1 damage to each creature with flying and each player. " +
                "If SN was kicked, it deals 4 damage to each creature with flying and each player instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.isKicked() ? 4 : 1;
            CREATURE_WITH_FLYING.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            }
            game.getAPNAP() each {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            }
        }
    }
]
