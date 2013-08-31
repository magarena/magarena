def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicChangeCountersAction(
        event.getPermanent(),
        MagicCounterType.Charge,
        1,
        true
    ));
} as MagicEventAction

def getEvent = {
    final MagicPermanent permanent ->
    new MagicEvent(
        permanent,
        action,
        "Put a charge counter on SN."
    );
}

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                getEvent(permanent) : MagicEvent.NONE;
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    cardOnStack.hasColor(MagicColor.Red)) ?
                getEvent(permanent) : MagicEvent.NONE;
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{3}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(source.getCounters(MagicCounterType.Charge)),
                this,
                "SN deals damage equal to the number of charge counters on it to target creature or player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicPermanent source=event.getPermanent();
                    final int amount=source.getCounters(MagicCounterType.Charge);
                    if (amount>0) {
                        final MagicDamage damage=new MagicDamage(source,target,amount);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            });
        }
    }
]
