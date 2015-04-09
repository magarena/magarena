[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature, where X is the number of creatures on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=game.getNrOfPermanents(MagicType.Creature);
            CREATURE
            .filter(game, event.getPlayer()) each {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            }
        }
    }
]
