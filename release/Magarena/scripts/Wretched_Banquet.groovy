[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target creature\$ if it has the least power or is " +
                "tied for least power among creatures on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                            event.getPlayer(),
                            MagicTargetFilter.TARGET_CREATURE);
                    final int power = creature.getPower();
                    boolean least = true;
                    for (final MagicPermanent permanent : targets) {
                        if (permanent.getPower() < power) {
                            least = false;
                            break;
                        }
                    }
                    if (least) {
                        game.doAction(new MagicDestroyAction(creature));
                    }
                }
            });
        }
    }
]
