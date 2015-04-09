[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_CREATURE),
                    MagicDestroyTargetPicker.DestroyNoRegen,
                    this,
                    "PN may\$ sacrifice SN. " +
                    "If you do, destroy target creature\$. It can't be regenerated."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                event.processTargetPermanent(game, {
                    game.doAction(MagicChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                    game.doAction(new MagicDestroyAction(it));
                });
            }
        }
    }
]
