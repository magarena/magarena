[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source=damage.getSource();
            return (source.hasSubType(MagicSubType.Sliver)  &&
                    damage.isCombat() &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    source.getController(),
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "PN may\$ draw a card."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }
    }
]
