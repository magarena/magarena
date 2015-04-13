[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(5,5),
                this,
                "Target creature\$ get -5/-5 until end of turn. " +
                "SN deals 5 damage to that creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,-5,-5));
                game.doAction(new MagicDealDamageAction(event.getSource(),it.getController(),5)); 
            });
        }
    }
]
