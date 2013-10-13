[
    new MagicAdditionalCost() {
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_GOBLIN);
        }
    },
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(5,5),
                this,
                "Target creature\$ get -5/-5 until end of turn. " +
                "SN deals 5 damage to that creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    game.doAction(new MagicChangeTurnPTAction(targetPermanent,-5,-5));
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        targetPermanent.getController(),
                        5
                    );
                    game.doAction(new MagicDealDamageAction(damage)); 
                }
            });
        }
    }
]
