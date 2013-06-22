[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_UNTAPPED_CREATURE,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target untapped creature\$. That creature deals damage equal to its power to its controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicTapAction(creature,true));
                    final MagicDamage damage=new MagicDamage(creature,creature.getController(),creature.getPower());
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
