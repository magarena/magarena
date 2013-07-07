[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{R}")),
                        MagicTargetChoice.NEG_TARGET_CREATURE
                    ),
                    new MagicNoCombatTargetPicker(false,true,false),
                    this,
                    "You may\$ pay {R}\$. If you do, target creature\$ can't block this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicGainAbilityAction(creature,MagicAbility.CannotBlock));
                    }
                });
            }
        }
    }
]
