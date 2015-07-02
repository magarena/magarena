def ANOTHER_TAPPED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target != source && target.isTapped() && target.isCreature() && target.isFriend(player);
    } 
};

def ANOTHER_TARGET_TAPPED_CREATURE_YOU_CONTROL = new MagicTargetChoice(
    ANOTHER_TAPPED_CREATURE_YOU_CONTROL,
    "another target tapped creature you control"
);

[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                ANOTHER_TARGET_TAPPED_CREATURE_YOU_CONTROL,
                this,
                "Whenever SN attacks, put a +1/+1 counter on another target tapped creature you control\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeCountersAction(it,MagicCounterType.PlusOne,1));
            });
        }
    },
    new MagicWouldBeMovedTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            if (act.card.hasType(MagicType.Creature) &&
                act.card.isEnemy(permanent) &&
                act.getToLocation() == MagicLocationType.Graveyard) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    }
]
