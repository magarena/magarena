[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +X/+X until end of turn, where X is the highest converted mana cost "+
                "among permanents PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            int X = 0;
            PERMANENT_YOU_CONTROL.filter(player) each {
                X = Math.max(X, it.getConvertedCost());
            }
            game.logAppendValue(player, X);
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, X, X));
            });
        }
    }
]
