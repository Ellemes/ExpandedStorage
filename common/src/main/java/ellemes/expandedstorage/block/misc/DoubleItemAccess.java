package ellemes.expandedstorage.block.misc;

import ellemes.expandedstorage.block.strategies.ItemAccess;

public interface DoubleItemAccess extends ItemAccess {
    Object getSingle();

    void setOther(DoubleItemAccess other);

    boolean hasCachedAccess();
}
