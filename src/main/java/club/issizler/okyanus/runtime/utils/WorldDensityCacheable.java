package club.issizler.okyanus.runtime.utils;

import java.util.Map;

public interface WorldDensityCacheable {

    public Map<ExplosionCacheKey, Float> getExplosionDensityCache();

}
