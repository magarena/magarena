def EventAction = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new MagicGainAbilityAction(it,MagicAbility.DoubleStrike));
    });
};

def Event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        MagicTargetChoice.PosOther("target Warrior creature you control", permanent),
        MagicFirstStrikeTargetPicker.create(),
        EventAction,
        "Another target Warrior creature PN controls\$ gains double strike until end of turn."
    );
};

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return Event(permanent);
        }
    },
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return Event(permanent);
        }
    }
]
