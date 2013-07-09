[
   new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isEnemy(permanent) &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}{B}"))
                    ),
                    otherPermanent.getCard(),
                    this,
                    "You may\$ pay {1}{B}\$. If you do, return RN to the battlefield under your control. " +
                    "If it's a creature, it's a Zombie in addition to its other creature types."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicCard card = event.getRefCard();
                if (card.getOwner().getGraveyard().contains(card)) {
                    final MagicPlayCardAction action = new MagicPlayCardAction(card,event.getPlayer(),MagicPlayCardAction.NONE);
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(action);
                    final MagicPermanent permanent = action.getPermanent();
                    if (permanent.isValid()) {
                        game.doAction(new MagicAddStaticAction(permanent,MagicStatic.Zombie));
                    }
                }
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump,true),
        "Regen"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{1}{B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_ZOMBIE,
                MagicRegenerateTargetPicker.getInstance(),
                this,
                "Regenerate target Zombie\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRegenerateAction(creature));
                }
            });
        }
    }
]
