package io.github.pheonixvx.bettertridentreturn;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;

public interface TridentItemInterface {
    static void checkStack(PlayerEntity player, ItemStack stack) {
        if(stack.getItem() instanceof TridentItem && EnchantmentHelper.getLevel(Enchantments.LOYALTY, stack) > 0) {
            if(stack.getTag() != null) {
                if(stack.getTag().contains("slot_thrown_from", 3)) {
                    int slot = stack.getTag().getInt("slot_thrown_from");
                    int curSlot = getSlotFor(player.inventory, stack);
                    if(slot != curSlot) {
                        if(slot == -1) {
                            ItemStack fromSlot = player.getOffHandStack();
                            if(fromSlot == null || fromSlot.isEmpty()) {
                                player.inventory.removeStack(curSlot);
                                stack.getTag().remove("slot_thrown_from");
                                player.inventory.offHand.set(0, stack);
                            }
                        } else if(slot != -2) {
                            ItemStack fromSlot = player.inventory.getStack(slot);
                            if(fromSlot == null || fromSlot.isEmpty()) {
                                player.inventory.removeStack(curSlot);
                                stack.getTag().remove("slot_thrown_from");
                                player.inventory.setStack(slot, stack);
                            }
                        }
                    }
                }
            }
        }
    }

    static int getSlotFor(PlayerInventory inv, ItemStack stack) {
        for(int i = 0; i < inv.main.size(); ++i) {
            if (!inv.main.get(i).isEmpty() && stackEqualExact(stack, inv.main.get(i))) {
                return i;
            }
        }
        if(!inv.offHand.get(0).isEmpty() && stackEqualExact(stack, inv.offHand.get(0))) {
            return -1;
        }

        return -2;
    }

    static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areTagsEqual(stack1, stack2);
    }
}
