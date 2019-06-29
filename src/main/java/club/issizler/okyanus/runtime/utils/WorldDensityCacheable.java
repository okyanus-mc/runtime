package club.issizler.okyanus.runtime.utils;

import java.util.Map;

public interface WorldDensityCacheable {

    Map<ExplosionCacheKey, Float> getExplosionDensityCache();

}
