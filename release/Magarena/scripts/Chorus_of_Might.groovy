[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),    
                this,
                "Until end of turn, target creature\$ gets +1/+1 for each creature PN controls and gains trample."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicType.Creature);
                game.doAction(new MagicChangeTurnPTAction(it, amount, amount));
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Trample));
            });
        }
    }
]
