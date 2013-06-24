[
    new MagicWhenYouCastSpiritOrArcaneTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(MagicTargetChoice.NEG_TARGET_PLAYER),
                spell.getConvertedCost(),
                this,
                "PN may\$ have target player\$ put the top RN cards of his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game,new MagicPlayerAction() {
                    public void doAction(final MagicPlayer player) {
                        game.doAction(new MagicMillLibraryAction(player, event.getRefInt()));
                    }
                });
            }
        }
    }
]
