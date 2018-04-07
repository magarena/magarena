package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

import java.util.ArrayList;
import java.util.Collection;
import magic.model.MagicMessage;

public class DestroyAction extends MagicAction {

    private final Collection<MagicPermanent> targets = new ArrayList<>();
    private int numDestroyed = 0;

    public DestroyAction(final MagicPermanent permanent) {
        this.targets.add(permanent);
    }

    public DestroyAction(final Collection<MagicPermanent> targets) {
        this.targets.addAll(targets);
    }

    @Override
    public void doAction(final MagicGame game) {
        final Collection<MagicPermanent> toBeDestroyed = new ArrayList<>();
        for (final MagicPermanent permanent : targets) {
            boolean destroy = true;

            // Indestructible
            if (destroy && permanent.hasAbility(MagicAbility.Indestructible)) {
                destroy = false;
            }

            // Regeneration
            if (destroy && permanent.isRegenerated()) {
                game.logAppendMessage(
                    permanent.getController(),
                    MagicMessage.format("%s is regenerated.", permanent)
                );
                game.doAction(new TapAction(permanent));
                game.doAction(new RemoveAllDamageAction(permanent));
                game.doAction(new RemoveFromCombatAction(permanent));
                game.doAction(ChangeStateAction.Clear(permanent,MagicPermanentState.Regenerated));
                destroy = false;
            }

            // Totem armor
            if (destroy && permanent.isEnchanted()) {
                for (final MagicPermanent aura : permanent.getAuraPermanents()) {
                    if (aura.hasAbility(MagicAbility.TotemArmor)) {
                        game.logAppendMessage(
                            permanent.getController(),
                            MagicMessage.format("Remove all damage from %s.", permanent)
                        );
                        game.doAction(new RemoveAllDamageAction(permanent));
                        toBeDestroyed.add(aura);
                        destroy = false;
                        //Only the first aura with totem armor will be
                        //destroyed.  If there are multiple auras with totem
                        //armor, player can choose the one to be destroyed, but
                        //this is not implemented
                        break;
                    }
                }
            }

            if (destroy) {
                toBeDestroyed.add(permanent);
            }
        }

        numDestroyed = toBeDestroyed.size();

        for (final MagicPermanent permanent : toBeDestroyed) {
            game.logAppendMessage(
                permanent.getController(),
                MagicMessage.format("%s is destroyed.", permanent)
            );
        }

        game.doAction(new RemoveAllFromPlayAction(toBeDestroyed, MagicLocationType.Graveyard));
    }

    public int getNumDestroyed() {
        return numDestroyed;
    }

    public boolean isDestroyed() {
        return numDestroyed == 1;
    }

    @Override
    public void undoAction(final MagicGame game) {}
}
