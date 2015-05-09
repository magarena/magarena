[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature with horsemanship and each player. (X="+payedCost.getX()+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int amount = event.getCardOnStack().getX();
            CREATURE_WITH_HORSEMANSHIP.filter(event) each {
                game.doAction(new DealDamageAction(source, it, amount));
            }
            game.getAPNAP() each {
                game.doAction(new DealDamageAction(source, it, amount));
            }
        }
    }
]
