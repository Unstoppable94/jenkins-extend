package com.wingarden.cicd.jenkins.common.utils;

public interface SensibleClone<T extends SensibleClone<T>> extends Cloneable {
  public T sensibleClone();
}
