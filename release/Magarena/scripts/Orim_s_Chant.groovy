[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ can't cast spells this turn. If SN was kicked, creatures can't attack this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new ChangePlayerStateAction(it, MagicPlayerState.CantCastSpells));
                if (event.isKicked()) {
                    CREATURE.filter(event) each {
                        game.doAction(new GainAbilityAction(it, MagicAbility.CannotAttack));
                    }
                }
            });
        }
    }
]
