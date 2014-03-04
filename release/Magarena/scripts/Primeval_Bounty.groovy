[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (cardOnStack.isFriend(permanent) && cardOnStack.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 3/3 green Beast creature token onto the battlefield."
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPermanent().getController(),TokenCardDefinitions.get("3/3 green Beast creature token")));
        }
    },
    
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (cardOnStack.isFriend(permanent) && !cardOnStack.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicCountersTargetPicker.create(),
                    this,
                    "PN puts three +1/+1 counters on target creature PN controls\$."
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,3,true));
            });
        }
    },
    
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains 3 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPermanent().getController(),3));
        }
    }
]
