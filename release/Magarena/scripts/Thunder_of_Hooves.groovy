[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature without flying and each player, where X is the number of Beasts on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int amount = game.getNrOfPermanents(MagicSubType.Beast);
            game.logAppendX(event.getPlayer(),amount);
            CREATURE_WITHOUT_FLYING.filter(event) each {
                game.doAction(new DealDamageAction(source,it,amount));
            }
            game.getAPNAP() each {
                game.doAction(new DealDamageAction(source,it,amount));
            }
        }
    }
]
