[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    new MagicMayChoice(NEG_TARGET_CREATURE_OR_PLAYER),
                    new MagicDamageTargetPicker(otherPermanent.getPower()),
                    otherPermanent,
                    this,
                    "PN may\$ have RN deal damage equal to its power to target creature or player\$."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game, {
                    final MagicPermanent source = event.getRefPermanent();
                    game.doAction(new DealDamageAction(source, it, source.getPower()));
                });
            }
        }
    }
]
