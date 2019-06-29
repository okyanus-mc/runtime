package club.issizler.okyanus.runtime.utils.accessors;

import club.issizler.okyanus.runtime.utils.ExplosionCacheKey;

import java.util.Map;

public interface WorldDensityCacheable {

    Map<ExplosionCacheKey, Float> getExplosionDensityCache();

}
