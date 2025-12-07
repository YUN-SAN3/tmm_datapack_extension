package com.example.tmmextension.mixin;

import com.example.tmmextension.AreaConfiguration;
import com.example.tmmextension.AreaConfigurationManager;
import dev.doctor4t.trainmurdermystery.cca.AreasWorldComponent;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;

@Mixin(AreasWorldComponent.class)
public class AreasWorldComponentMixin {

    @Inject(method = "getSpawnPos", at = @At("HEAD"), cancellable = true)
    private void injectGetSpawnPos(CallbackInfoReturnable<AreasWorldComponent.PosWithOrientation> cir) {
        if (AreaConfigurationManager.hasConfiguration()) {
            AreaConfiguration config = AreaConfigurationManager.getCurrentConfiguration();
            try {
                Constructor<AreasWorldComponent.PosWithOrientation> constructor = AreasWorldComponent.PosWithOrientation.class.getDeclaredConstructor(double.class, double.class, double.class, float.class, float.class);
                constructor.setAccessible(true);
                AreasWorldComponent.PosWithOrientation pos = constructor.newInstance(
                        config.getSpawnPos().getPos().x,
                        config.getSpawnPos().getPos().y,
                        config.getSpawnPos().getPos().z,
                        config.getSpawnPos().getYaw(),
                        config.getSpawnPos().getPitch()
                );
                cir.setReturnValue(pos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Inject(method = "getSpectatorSpawnPos", at = @At("HEAD"), cancellable = true)
    private void injectGetSpectatorSpawnPos(CallbackInfoReturnable<AreasWorldComponent.PosWithOrientation> cir) {
        if (AreaConfigurationManager.hasConfiguration()) {
            AreaConfiguration config = AreaConfigurationManager.getCurrentConfiguration();
            try {
                Constructor<AreasWorldComponent.PosWithOrientation> constructor = AreasWorldComponent.PosWithOrientation.class.getDeclaredConstructor(double.class, double.class, double.class, float.class, float.class);
                constructor.setAccessible(true);
                AreasWorldComponent.PosWithOrientation pos = constructor.newInstance(
                        config.getSpectatorSpawnPos().getPos().x,
                        config.getSpectatorSpawnPos().getPos().y,
                        config.getSpectatorSpawnPos().getPos().z,
                        config.getSpectatorSpawnPos().getYaw(),
                        config.getSpectatorSpawnPos().getPitch()
                );
                cir.setReturnValue(pos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Inject(method = "getReadyArea", at = @At("HEAD"), cancellable = true)
    private void injectGetReadyArea(CallbackInfoReturnable<Box> cir) {
        if (AreaConfigurationManager.hasConfiguration()) {
            AreaConfiguration config = AreaConfigurationManager.getCurrentConfiguration();
            cir.setReturnValue(config.getReadyArea());
        }
    }

    @Inject(method = "getPlayAreaOffset", at = @At("HEAD"), cancellable = true)
    private void injectGetPlayAreaOffset(CallbackInfoReturnable<Vec3d> cir) {
        if (AreaConfigurationManager.hasConfiguration()) {
            AreaConfiguration config = AreaConfigurationManager.getCurrentConfiguration();
            cir.setReturnValue(config.getPlayAreaOffset());
        }
    }

    @Inject(method = "getPlayArea", at = @At("HEAD"), cancellable = true)
    private void injectGetPlayArea(CallbackInfoReturnable<Box> cir) {
        if (AreaConfigurationManager.hasConfiguration()) {
            AreaConfiguration config = AreaConfigurationManager.getCurrentConfiguration();
            cir.setReturnValue(config.getPlayArea());
        }
    }

    @Inject(method = "getResetTemplateArea", at = @At("HEAD"), cancellable = true)
    private void injectGetResetTemplateArea(CallbackInfoReturnable<Box> cir) {
        if (AreaConfigurationManager.hasConfiguration()) {
            AreaConfiguration config = AreaConfigurationManager.getCurrentConfiguration();
            cir.setReturnValue(config.getResetTemplateArea());
        }
    }

    @Inject(method = "getResetPasteArea", at = @At("HEAD"), cancellable = true)
    private void injectGetResetPasteArea(CallbackInfoReturnable<Box> cir) {
        if (AreaConfigurationManager.hasConfiguration()) {
            AreaConfiguration config = AreaConfigurationManager.getCurrentConfiguration();
            cir.setReturnValue(config.getResetPasteArea());
        }
    }
}