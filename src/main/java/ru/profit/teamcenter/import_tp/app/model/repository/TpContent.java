package ru.profit.teamcenter.import_tp.app.model.repository;

import java.util.List;

public interface TpContent {
    List<String> getContent(String key) throws Exception;
}
