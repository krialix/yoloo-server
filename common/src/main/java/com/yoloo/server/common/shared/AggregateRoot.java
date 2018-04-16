package com.yoloo.server.common.shared;

import java.io.Serializable;

public abstract class AggregateRoot<E extends Entity, ID extends Serializable>
    implements Entity<E, ID> {


}
