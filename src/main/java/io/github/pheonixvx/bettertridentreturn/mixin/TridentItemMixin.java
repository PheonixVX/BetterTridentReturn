package io.github.pheonixvx.bettertridentreturn.mixin;

import io.github.pheonixvx.bettertridentreturn.TridentItemInterface;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public class TridentItemMixin implements TridentItemInterface {

    @Inject(at = @At("HEAD"), method = "onStoppedUsing")
    public void onThrown(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            if (stack.getItem() instanceof TridentItem && EnchantmentHelper.getLevel(Enchantments.LOYALTY, stack) > 0) {
                int i = 72000 - remainingUseTicks;
                if (i >= 10) {
                    int j = EnchantmentHelper.getRiptide(stack);
                    if (j <= 0 || player.isWet()) {
                        ItemStack newStack = stack;
                        if(newStack.getTag() == null) {
                            newStack.setTag(new CompoundTag());
                        }
                        newStack.getTag().putInt("slot_thrown_from", -3); // unique identifier
                        int slot = TridentItemInterface.getSlotFor(player.inventory, stack);
                        newStack.getTag().putInt("slot_thrown_from", slot);
                    }
                }
            }
        }
    }
}
