package magic.model.choice;

import magic.model.MagicCostManaType;
import magic.model.MagicManaCost;

import java.util.Arrays;
import java.util.List;

public class MagicBuilderManaCost {

    public static final MagicBuilderManaCost ZERO_COST=new MagicBuilderManaCost();

    private final int[] amounts;
    private MagicCostManaType[] compressedTypes;
    private int[] compressedAmounts;
    private int typeCount;
    private int minimumAmount;
    private int XCount;

    public MagicBuilderManaCost() {
        amounts=new int[MagicCostManaType.NR_OF_TYPES];
        compressedTypes=new MagicCostManaType[0];
        compressedAmounts=new int[0];
        typeCount=0;
        minimumAmount=0;
        XCount=0;
    }

    public MagicBuilderManaCost(final MagicManaCost cost) {
        this();
        cost.addTo(this);
    }

    public MagicBuilderManaCost(final MagicBuilderManaCost cost) {
        amounts=Arrays.copyOf(cost.amounts,cost.amounts.length);
        compressedTypes=Arrays.copyOf(cost.compressedTypes,cost.compressedTypes.length);
        compressedAmounts=Arrays.copyOf(cost.compressedAmounts,cost.compressedAmounts.length);
        typeCount=cost.typeCount;
        minimumAmount=cost.minimumAmount;
        XCount=cost.XCount;
    }

    public void compress() {
        typeCount = 0;
        for (final int amt : amounts) {
            if (amt != 0) {
                typeCount++;
            }
        }

        compressedTypes=new MagicCostManaType[typeCount];
        compressedAmounts=new int[typeCount];
        int j=0;

        // Ordered from most restrictive to least restrictive.
        for (int i = MagicCostManaType.NR_OF_TYPES - 1; i >= 0; i--) {
            final int amt = amounts[i];
            if (amt != 0) {
                compressedTypes[j]=MagicCostManaType.values()[i];
                compressedAmounts[j]=amt;
                j++;
            }
        }
    }

    public MagicCostManaType[] getTypes() {
        return compressedTypes;
    }

    public int[] getAmounts() {
        return compressedAmounts;
    }

    public int getMinimumAmount() {
        return minimumAmount;
    }

    boolean hasX() {
        return XCount > 0;
    }

    public void setXCount(final int amount) {
        addType(MagicCostManaType.Generic,amount);
        XCount = amount;
    }

    int getX(final int amount) {
        return hasX() ? (amount - minimumAmount) / XCount + 1 : 0;
    }

    boolean validX(final int amount) {
        return ((amount - compressedAmounts[typeCount - 1]) % XCount) == 0;
    }

    public boolean isEmpty() {
        return typeCount == 0;
    }

    public void addType(final MagicCostManaType type,final int amount) {
        assert type == MagicCostManaType.Generic || amount >= 0 : "amount of mana to add is negative: " + type + ", " + amount;
        final int i = type.ordinal();
        amounts[i] += amount;
        minimumAmount += amount;
    }

    public void removeType(final MagicCostManaType type,final int amount) {
        assert amount >= 0 : "amount of mana to add is negative: " + type + ", " + amount;
        final int i = type.ordinal();
        amounts[i] -= amount;
        minimumAmount -= amount;
        assert amounts[i] >= 0 : "amounts[i] is negative: amounts[i] = " + amounts[i];
    }

    void addTypes(final List<MagicCostManaType> types) {
        for (final MagicCostManaType type : types) {
            addType(type,1);
        }
        compress();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < compressedTypes.length; i++) {
            builder
                .append(compressedTypes[i])
                .append('=')
                .append(compressedAmounts[i])
                .append(' ');
        }
        builder
            .append("Total=")
            .append(minimumAmount);
        return builder.toString();
    }
}
