[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Target creature\$ gets +2/+2 until end of turn for each of its colors."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (it.hasColor(MagicColor.Black)) {
                    game.doAction(new ChangeTurnPTAction(it,2,2));
                }
                if (it.hasColor(MagicColor.Blue)) {
                    game.doAction(new ChangeTurnPTAction(it,2,2));
                }
                if (it.hasColor(MagicColor.Green)) {
                    game.doAction(new ChangeTurnPTAction(it,2,2));
                }
                if (it.hasColor(MagicColor.Red)) {
                    game.doAction(new ChangeTurnPTAction(it,2,2));
                }
                if (it.hasColor(MagicColor.White)) {
                    game.doAction(new ChangeTurnPTAction(it,2,2));
                }
            });
        }
    }
]
