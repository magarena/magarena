[
    new EntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN loses life equal to his or her life total."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int lossAmount = player.getLife();
            final int amount = 0 - lossAmount;
            game.doAction(new ChangeLifeAction(player, amount));
            game.logAppendValue(player, lossAmount)
        }
    },

    new IfPlayerWouldLoseTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final LoseGameAction loseAct) {
            if (permanent.isController(loseAct.getPlayer()) && loseAct.getReason() == LoseGameAction.LIFE_REASON) {
                loseAct.setPlayer(MagicPlayer.NONE);
            }
            return MagicEvent.NONE;
        }
    },

    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            int amount = 0;
            if (permanent.isController(act.getPlayer()) && act.getLifeChange() > 0) {
                amount = act.getLifeChange();
                act.setLifeChange(0);
                return new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN draws RN cards."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), event.getRefInt()));
        }
    },

    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            final int amount = Math.abs(act.getLifeChange());
            return permanent.isController(act.getPlayer()) && act.getLifeChange() < 0 && act.isDamage() ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN sacrifices RN nontoken permanents."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicRepeatedCostEvent(
                event.getSource(),
                new MagicTargetChoice("a nontoken permanent to sacrifice"),
                event.getRefInt(),
                MagicChainEventFactory.Sac
            );
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new LoseGameAction(event.getPlayer()," lost the game because of not being able to sacrifice enough nontoken permanents."))
            }
        }
    }
]
