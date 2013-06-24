
[
    new MagicPlaneswalkerActivation(2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PERMANENT_YOU_OWN,
                this,
                "Exile target permanent you own. Return it to the battlefield under your control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicExileUntilEndOfTurnAction(perm));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(-1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Creatures are unblockable this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            outerGame.doAction(new MagicAddStaticAction(
                new MagicStatic(
                    MagicLayer.Ability,
                    MagicTargetFilter.TARGET_CREATURE,
                    MagicStatic.UntilEOT) {
                    @Override
                    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                        flags.add(MagicAbility.Unblockable);
                    }
                }
            ));
        }
    },
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Whenever you cast a spell, exile target permanent.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final MagicPlayer you = outerEvent.getPlayer();
            outerGame.doAction(new MagicAddTriggerAction(
                new MagicWhenOtherSpellIsCastTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
                        return cardOnStack.getController().getId() == you.getId() ?
                            new MagicEvent(
                                cardOnStack,
                                MagicTargetChoice.NEG_TARGET_PERMANENT,
                                MagicExileTargetPicker.create(),
                                this,
                                "Exile target permanent\$."
                            ):
                            MagicEvent.NONE;
                    }
                    @Override
                    public void executeEvent(final MagicGame game,final MagicEvent event) {
                        event.processTargetPermanent(game,new MagicPermanentAction() {
                            public void doAction(final MagicPermanent perm) {
                                game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.Exile));
                            }
                        });
                    }
                }
            ));
        }
    }
]
