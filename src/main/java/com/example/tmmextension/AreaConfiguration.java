package com.example.tmmextension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class AreaConfiguration {
    public static final Codec<AreaConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PosWithOrientation.CODEC.fieldOf("spawn_pos").forGetter(config -> config.spawnPos),
            PosWithOrientation.CODEC.fieldOf("spectator_spawn_pos").forGetter(config -> config.spectatorSpawnPos),
            BoxCodec.CODEC.fieldOf("ready_area").forGetter(config -> config.readyArea),
            Vec3dCodec.CODEC.fieldOf("play_area_offset").forGetter(config -> config.playAreaOffset),
            BoxCodec.CODEC.fieldOf("play_area").forGetter(config -> config.playArea),
            BoxCodec.CODEC.fieldOf("reset_template_area").forGetter(config -> config.resetTemplateArea),
            BoxCodec.CODEC.fieldOf("reset_paste_area").forGetter(config -> config.resetPasteArea)
    ).apply(instance, AreaConfiguration::new));

    private final PosWithOrientation spawnPos;
    private final PosWithOrientation spectatorSpawnPos;
    private final Box readyArea;
    private final Vec3d playAreaOffset;
    private final Box playArea;
    private final Box resetTemplateArea;
    private final Box resetPasteArea;

    public AreaConfiguration(PosWithOrientation spawnPos, PosWithOrientation spectatorSpawnPos, Box readyArea,
                             Vec3d playAreaOffset, Box playArea, Box resetTemplateArea, Box resetPasteArea) {
        this.spawnPos = spawnPos;
        this.spectatorSpawnPos = spectatorSpawnPos;
        this.readyArea = readyArea;
        this.playAreaOffset = playAreaOffset;
        this.playArea = playArea;
        this.resetTemplateArea = resetTemplateArea;
        this.resetPasteArea = resetPasteArea;
    }

    // Getters
    public PosWithOrientation getSpawnPos() {
        return spawnPos;
    }

    public PosWithOrientation getSpectatorSpawnPos() {
        return spectatorSpawnPos;
    }

    public Box getReadyArea() {
        return readyArea;
    }

    public Vec3d getPlayAreaOffset() {
        return playAreaOffset;
    }

    public Box getPlayArea() {
        return playArea;
    }

    public Box getResetTemplateArea() {
        return resetTemplateArea;
    }

    public Box getResetPasteArea() {
        return resetPasteArea;
    }

    @Override
    public String toString() {
        return "AreaConfiguration{" +
                "spawnPos=" + spawnPos +
                ", spectatorSpawnPos=" + spectatorSpawnPos +
                ", readyArea=" + readyArea +
                ", playAreaOffset=" + playAreaOffset +
                ", playArea=" + playArea +
                ", resetTemplateArea=" + resetTemplateArea +
                ", resetPasteArea=" + resetPasteArea +
                '}';
    }

    // Nested codec classes
    public static class PosWithOrientation {
        public static final Codec<PosWithOrientation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Vec3dCodec.CODEC.fieldOf("pos").forGetter(pos -> pos.pos),
                Codec.FLOAT.fieldOf("yaw").forGetter(pos -> pos.yaw),
                Codec.FLOAT.fieldOf("pitch").forGetter(pos -> pos.pitch)
        ).apply(instance, PosWithOrientation::new));

        private final Vec3d pos;
        private final float yaw;
        private final float pitch;

        public PosWithOrientation(Vec3d pos, float yaw, float pitch) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public Vec3d getPos() {
            return pos;
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }

        @Override
        public String toString() {
            return "PosWithOrientation{pos=" + pos + ", yaw=" + yaw + ", pitch=" + pitch + "}";
        }
    }

    private static class BoxCodec {
        public static final Codec<Box> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.DOUBLE.fieldOf("minX").forGetter(box -> box.minX),
                Codec.DOUBLE.fieldOf("minY").forGetter(box -> box.minY),
                Codec.DOUBLE.fieldOf("minZ").forGetter(box -> box.minZ),
                Codec.DOUBLE.fieldOf("maxX").forGetter(box -> box.maxX),
                Codec.DOUBLE.fieldOf("maxY").forGetter(box -> box.maxY),
                Codec.DOUBLE.fieldOf("maxZ").forGetter(box -> box.maxZ)
        ).apply(instance, Box::new));
        
        // 添加 getter 方法以满足 RecordCodecBuilder 的要求
        public static double getMinX(Box box) { return box.minX; }
        public static double getMinY(Box box) { return box.minY; }
        public static double getMinZ(Box box) { return box.minZ; }
        public static double getMaxX(Box box) { return box.maxX; }
        public static double getMaxY(Box box) { return box.maxY; }
        public static double getMaxZ(Box box) { return box.maxZ; }
    }

    private static class Vec3dCodec {
        public static final Codec<Vec3d> CODEC = Codec.either(
                Codec.DOUBLE.listOf().comapFlatMap(
                        list -> {
                            if (list.size() != 3) {
                                return com.mojang.serialization.DataResult.error(() -> "Vec3d array needs exactly 3 values");
                            }
                            return com.mojang.serialization.DataResult.success(new Vec3d(list.get(0), list.get(1), list.get(2)));
                        },
                        vec -> java.util.List.of(vec.getX(), vec.getY(), vec.getZ())
                ),
                RecordCodecBuilder.<Vec3d>create(instance -> instance.group(
                        Codec.DOUBLE.fieldOf("x").forGetter(Vec3d::getX),
                        Codec.DOUBLE.fieldOf("y").forGetter(Vec3d::getY),
                        Codec.DOUBLE.fieldOf("z").forGetter(Vec3d::getZ)
                ).apply(instance, Vec3d::new))
        ).xmap(
                either -> either.map(vec -> vec, vec -> vec),
                vec -> com.mojang.datafixers.util.Either.right(vec)
        );
    }
}