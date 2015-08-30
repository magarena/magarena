[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gains trample and gets +X/+0 until end of turn, where X is that creature's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = it.getConvertedCost();
                game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
                game.doAction(new ChangeTurnPTAction(
                    it,
                    amount,
                    0
                ));
                game.logAppendX(event.getPlayer(),amount);
            });
        }
    }
]
