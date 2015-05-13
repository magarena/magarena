[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 2 damage to each attacking creature and 1 damage to PN and each creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            ATTACKING_CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(source,it,2));
            }
            game.doAction(new DealDamageAction(source,event.getPlayer(),1));
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new DealDamageAction(source,it,1));
            }
        }
    }
]
