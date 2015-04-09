[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "If target creature\$ has toughness 5 or greater, it gets +4/-4 until end of turn. "+
                "Otherwise, it gets +4/-X until end of turn, where X is its toughness minus 1."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int toughness = it.getToughness();
                if (toughness >= 5) {
                    game.doAction(new MagicChangeTurnPTAction(it,4,-4));
                } else {
                    game.doAction(new MagicChangeTurnPTAction(it,4,-(toughness-1)));
                }
            });
        }
    }
]
