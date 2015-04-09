def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Counter RN and put a depletion counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicCounterItemOnStackAction(event.getRefCardOnStack()));
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Depletion,1));
        }
    },
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.Depletion) >= 3;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new MagicPutStateTriggerOnStackAction(
                EFFECT.getEvent(source)
            ));
        }
    },
    new MagicWhenCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
            return new MagicEvent(
                card,
                new MagicMayChoice(NEG_TARGET_SPELL),
                this,
                "PN may\$ counter target spell\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCardOnStack(game, {
                    game.doAction(new MagicCounterItemOnStackAction(it));
                });
            }
        }
    }
]
