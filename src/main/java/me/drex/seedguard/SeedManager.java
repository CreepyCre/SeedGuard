package me.drex.seedguard;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.security.SecureRandom;

public class SeedManager {

    private static final SecureRandom random = new SecureRandom();
    private static final Reference2IntMap<Holder<StructureSet>> structureSeeds = new Reference2IntOpenHashMap<>();
    private static final Reference2LongMap<Holder<ConfiguredFeature<?, ?>>> featureSeeds = new Reference2LongOpenHashMap<>();

    public static void load(MinecraftServer server) {

        server.registryAccess().lookupOrThrow(Registries.STRUCTURE_SET).listElements().forEach(holder -> {
            structureSeeds.computeIfAbsent(holder, ignored -> random.nextInt());
        });
        server.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).listElements().forEach(holder -> {
            featureSeeds.computeIfAbsent(holder, ignored -> random.nextLong());
        });
    }

    public static int getStructureSeed(Holder<StructureSet> holder) {
        return structureSeeds.computeIfAbsent(holder, ignored -> random.nextInt()) + (int) System.nanoTime();
    }

    public static long getFeatureSeed(Holder<ConfiguredFeature<?, ?>> holder) {
        return featureSeeds.computeIfAbsent(holder, ignored -> random.nextLong()) + System.nanoTime();
    }
}
