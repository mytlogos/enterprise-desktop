package com.mytlogos.enterprisedesktop.background.resourceLoader;

interface Dependant {
    Object getValue();

    Runnable getRunBefore();
}
