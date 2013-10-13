[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(4,true),
                this,
                "SN deals 4 damage to target player\$. " +
                "If you control a Giant, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,4);
                    game.doAction(new MagicDealDamageAction(damage));
                    if(event.getPlayer().getNrOfPermanents(MagicSubType.Giant) > 0){
                        game.doAction(new MagicDrawAction(event.getPlayer()));
                    }
                }
            });
        }
    }
]
