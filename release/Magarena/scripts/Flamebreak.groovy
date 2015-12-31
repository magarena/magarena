[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 3 damage to each creature without flying and each player. " +
                "Creatures dealt damage this way can't be regenerated this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            CREATURE_WITHOUT_FLYING.filter(event) each {
                final MagicDamage damage=new MagicDamage(source,it,3);
                damage.setNoRegeneration();
                game.doAction(new DealDamageAction(damage));
            }
            game.getAPNAP() each {
                game.doAction(new DealDamageAction(source,it,3));
            }
        }
    }
]
