[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ and each other creature that shares a color with it get +1/+1 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->

                final Set<MagicColor> targetColors = new HashSet<MagicColor>();
                for (final MagicColor color : MagicColor.values()) {
                    if(target.hasColor(color)) {
                        targetColors.add(color);
                    }
                }

                final Set<MagicPermanent> targetList = new HashSet<MagicPermanent>();
                for (final MagicPermanent creature : CREATURE.filter(event)) {
                    for (final MagicColor color : targetColors) {
                        if (creature.hasColor(color)) {
                            targetList.add(creature);
                            break;
                        }
                    }
                }

                for (final MagicPermanent creature : targetList) {
                    game.doAction(new ChangeTurnPTAction(creature, 1, 1));
                }
            });
        }
    }
]
