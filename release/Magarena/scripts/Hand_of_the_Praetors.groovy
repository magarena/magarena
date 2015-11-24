[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.isFriend(permanent) &&
                    cardOnStack.hasType(MagicType.Creature) &&
                    cardOnStack.hasAbility(MagicAbility.Infect)) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_PLAYER,
                    this,
                    "Target player\$ gets a poison counter."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new ChangePoisonAction(it,1));
            });
        }
    }
]
