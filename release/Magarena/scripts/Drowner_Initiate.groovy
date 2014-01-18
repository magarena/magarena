[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.hasColor(MagicColor.Blue)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}")),
                        MagicTargetChoice.NEG_TARGET_PLAYER
                    ),
                    this,
                    "PN may\$ pay {1}\$. If PN does, target player\$ puts the top two cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game, {
                    final MagicPlayer player ->
                    game.doAction(new MagicMillLibraryAction(player,2));
                });
            }
        }
    }
]
