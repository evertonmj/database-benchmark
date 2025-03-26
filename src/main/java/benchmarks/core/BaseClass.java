package benchmarks.core;

import utils.ConfigUtils;
import utils.annotations.ConfigProperty;

public class BaseClass<T> {
    public BaseClass () {
        ConfigUtils.loadConfig(this.getClass());
    }
}
