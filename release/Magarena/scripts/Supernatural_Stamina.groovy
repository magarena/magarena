def Return = MagicAbility.getAbilityList("When SN dies, return it to the battlefield tapped under its owner's control.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Until end of turn, target creature\$ gets +2/+0 " +
                "and gains \"When this creature dies, return it to the battlefield tapped under its owner's control.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, 2, 0));
                game.doAction(new GainAbilityAction(it, Return));
            });
        }
    }
]

