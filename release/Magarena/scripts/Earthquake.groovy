[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getX(),
                this,
                "SN deals RN damage to each creature without flying and each player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_WITHOUT_FLYING.filter(game) each {
                game.doAction(new DealDamageAction(event.getSource(),it,event.getRefInt()));
            }
            game.getAPNAP() each {
                game.doAction(new DealDamageAction(event.getSource(),it,event.getRefInt()));
            }
        }
    }
]
