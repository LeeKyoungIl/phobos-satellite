package me.phoboslabs.phobos.satellite.scanner.thread.storage;

import me.phoboslabs.phobos.satellite.scanner.vo.PhobosBaseModel;

public interface DataStoreProcess {

    void init();

    void saveData(PhobosBaseModel baseModel);

}
