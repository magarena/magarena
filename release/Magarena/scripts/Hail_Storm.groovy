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
            final MagicPlayer player = event.getPlayer();
            final MagicSource source = event.getSource();
            ATTACKING_CREATURE.filter(game) each {
                game.doAction(new MagicDealDamageAction(source,it,2));
            }
            game.doAction(new MagicDealDamageAction(source,player,1));
            CREATURE_YOU_CONTROL.filter(player) each {
                game.doAction(new MagicDealDamageAction(source,it,1));
            }
        }
    }
]
