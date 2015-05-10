[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature\$ if it has the least power or is " +
                "tied for least power among creatures on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final Collection<MagicPermanent> targets = CREATURE.filter(event);
                final int power = it.getPower();
                boolean least = true;
                for (final MagicPermanent permanent : targets) {
                    if (permanent.getPower() < power) {
                        least = false;
                        break;
                    }
                }
                if (least) {
                    game.doAction(new DestroyAction(it));
                }
            });
        }
    }
]
