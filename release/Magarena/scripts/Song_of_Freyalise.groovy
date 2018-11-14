def mana = MagicAbility.getAbility("{T}: Add one mana of any color.");

def c12Action = {
    final MagicGame outerGame, final MagicEvent outerEvent ->
    final MagicPlayer player = outerEvent.getPlayer();
    player.getPermanents().findAll({ it.hasType(MagicType.Creature) }).each {
        outerGame.doAction(new GainAbilityAction(it, mana, MagicStatic.Forever));

        final AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                if (upkeepPlayer.getId() == player.getId()) {
                    game.addDelayedAction(new LoseAbilityAction(permanent, mana));
                    game.addDelayedAction(new RemoveTriggerAction(permanent, this));
                }
                return MagicEvent.NONE;
            }
        }
        outerGame.doAction(new AddTriggerAction(it, cleanup));
    }
}

def c12Event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        c12Action,
        "Until your next turn, creatures PN controls gain \"{T}: Add one mana of any color.\""
    );
}

[
    new SagaChapterTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return c12Event(permanent);
        }
    },

    new SagaChapterTrigger(2) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return c12Event(permanent);
        }
    },

    new SagaChapterTrigger(3) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on each creature PN controls. Those creatures gain vigilance, trample, and indestructible until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            player.getPermanents().findAll({ it.hasType(MagicType.Creature) }).each {
                game.doAction(new ChangeCountersAction(player, it, MagicCounterType.PlusOne, 1));
                game.doAction(new GainAbilityAction(it, MagicAbility.Vigilance, MagicAbility.Trample, MagicAbility.Indestructible));
            }
        }
    }
]
