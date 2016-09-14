def filter = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return (target.isAttacking() || target.isBlocking()) &&
                target.hasSubType(MagicSubType.Dragon);
    }
};

def choice = new MagicTargetChoice(filter, "target attacking or blocking Dragon"); //Doesn't explicitly state Creature

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target attacking or blocking Dragon.\$ It can't be regenerated. PN gains life equal to its power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final int amount = it.getPower();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(ChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(it));
                game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
            });
        }
    }
]
