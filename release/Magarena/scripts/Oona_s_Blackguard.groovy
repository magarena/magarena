[
    new MagicWhenOtherComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            if (otherPermanent != permanent &&
                otherPermanent.isCreature() &&
                otherPermanent.isFriend(permanent) &&
                otherPermanent.hasSubType(MagicSubType.Rogue)) {
                game.doAction(ChangeCountersAction.Enters(otherPermanent,MagicCounterType.PlusOne,1));
            }
            return MagicEvent.NONE;
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().hasCounters(MagicCounterType.PlusOne) &&
                damage.isCombat() &&
                damage.getSource().isFriend(permanent) &&
                damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "RN discards a card."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getRefPlayer()));
        }
    }
]
