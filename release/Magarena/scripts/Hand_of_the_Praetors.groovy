[
    new MagicStatic(
        MagicLayer.ModPT,
        CREATURE_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target && target.hasAbility(MagicAbility.Infect);
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
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
