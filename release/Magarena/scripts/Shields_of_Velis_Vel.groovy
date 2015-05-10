[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_PLAYER,
                this,
                "Creatures target player\$ controls get +0/+1 and gains all creature types until the end of the turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                CREATURE_YOU_CONTROL.filter(it) each {
                    game.doAction(new ChangeTurnPTAction(it,0,1));
                    game.doAction(new AddStaticAction(it,MagicStatic.AllCreatureTypesUntilEOT));
                }
            });
        }
    }
]
