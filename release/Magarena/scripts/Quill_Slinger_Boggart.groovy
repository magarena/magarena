[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.hasSubType(MagicSubType.Kithkin)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.NEG_TARGET_PLAYER
                    ),
                    this,
                    "PN may\$ have target player\$ lose 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game, {
                    final MagicPlayer player ->
                    game.doAction(new MagicChangeLifeAction(player,-1));
                });
            }
        }
    }
]
