[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "Until end of turn, target creature\$ and each other creature that shares a color with it gain \"This creature can't block.\""
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
                    game.doAction(new GainAbilityAction(creature, MagicAbility.CannotBlock));
                }
            });
        }
    }
]
